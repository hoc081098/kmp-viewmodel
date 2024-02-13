@file:OptIn(ExperimentalCoroutinesApi::class)

package com.hoc081098.kmpviewmodelsample.products

import com.hoc081098.flowext.flatMapFirst
import com.hoc081098.flowext.flowFromSuspend
import com.hoc081098.flowext.interval
import com.hoc081098.flowext.startWith
import com.hoc081098.kmp.viewmodel.Closeable
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.wrapper.NonNullFlowWrapper
import com.hoc081098.kmp.viewmodel.wrapper.NonNullStateFlowWrapper
import com.hoc081098.kmp.viewmodel.wrapper.wrap
import com.hoc081098.kmpviewmodelsample.ProductItemUi
import com.hoc081098.kmpviewmodelsample.common.Immutable
import com.hoc081098.kmpviewmodelsample.common.SingleEventChannel
import com.hoc081098.kmpviewmodelsample.toProductItemUi
import io.github.aakira.napier.Napier
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn

@Immutable
data class ProductsState(
  val products: ImmutableList<ProductItemUi>,
  val isLoading: Boolean,
  val error: Throwable?,
  val isRefreshing: Boolean,
) {
  val hasContent: Boolean get() = products.isNotEmpty() && error == null

  companion object {
    val INITIAL = ProductsState(
      products = persistentListOf(),
      isLoading = true,
      error = null,
      isRefreshing = false,
    )
  }
}

sealed interface ProductSingleEvent {
  sealed interface Refresh : ProductSingleEvent {
    data object Success : Refresh
    data class Failure(val error: Throwable) : Refresh
  }
}

sealed interface ProductsAction {
  data object Load : ProductsAction
  data object Refresh : ProductsAction
}

private fun interface Reducer {
  operator fun invoke(state: ProductsState): ProductsState
}

class ProductsViewModel(
  private val getProducts: GetProducts,
  private val singleEventChannel: SingleEventChannel<ProductSingleEvent>,
) : ViewModel(
  Closeable { Napier.d("[DEMO] Closable 1 ...") },
  Closeable { Napier.d("[DEMO] Closable 2 ...") },
  singleEventChannel,
) {
  private val _actionFlow = MutableSharedFlow<ProductsAction>(Int.MAX_VALUE)

  val stateFlow: NonNullStateFlowWrapper<ProductsState>
  val eventFlow: NonNullFlowWrapper<ProductSingleEvent> = singleEventChannel.singleEventFlow.wrap()

  init {
    addCloseable { Napier.d("[DEMO] Closable 3 ...") }

    stateFlow = merge(
      _actionFlow
        .filterIsInstance<ProductsAction.Load>()
        .loadFlow(),
      _actionFlow
        .filterIsInstance<ProductsAction.Refresh>()
        .refreshFlow(),
    )
      .scan(ProductsState.INITIAL) { state, reducer -> reducer(state) }
      .onEach {
        Napier.d(
          "State: products=${it.products.size}, isLoading=${it.isLoading}," +
            " error=${it.error}, isRefreshing=${it.isRefreshing}",
        )
      }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ProductsState.INITIAL,
      )
      .wrap()
  }

  //region Handlers
  private fun Flow<ProductsAction.Refresh>.refreshFlow(): Flow<Reducer> =
    flatMapFirst {
      flowFromSuspend { getProducts() }
        .map { products ->
          singleEventChannel.sendEvent(ProductSingleEvent.Refresh.Success)

          Reducer { state ->
            state.copy(
              products = products
                .map { it.toProductItemUi() }
                .toImmutableList(),
              isRefreshing = false,
              error = null,
            )
          }
        }
        .catch { throwable ->
          singleEventChannel.sendEvent(ProductSingleEvent.Refresh.Failure(throwable))
          emit(Reducer { it.copy(isRefreshing = false) })
        }
        .startWith { Reducer { it.copy(isRefreshing = true) } }
    }

  private fun Flow<ProductsAction.Load>.loadFlow(): Flow<Reducer> =
    flatMapLatest {
      flowFromSuspend { getProducts() }
        .map { products ->
          Reducer { state ->
            state.copy(
              products = products
                .map { it.toProductItemUi() }
                .toImmutableList(),
              isLoading = false,
              error = null,
            )
          }
        }
        .catch { error ->
          emit(
            Reducer {
              it.copy(
                isLoading = false,
                error = error,
              )
            },
          )
        }
        .startWith {
          Reducer {
            it.copy(
              isLoading = true,
              error = null,
            )
          }
        }
    }
  //endregion

  fun dispatch(action: ProductsAction) {
    _actionFlow.tryEmit(action)
  }

  /**
   * Demo purpose only
   */
  @Suppress("unused") // Called from platform code
  val tickerFlow: StateFlow<String?> = interval(initialDelay = Duration.ZERO, period = 1.seconds)
    .map {
      if (it % 2 == 0L) {
        it.toString()
      } else {
        null
      }
    }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(@Suppress("MagicNumber") 5_000),
      initialValue = null,
    )
}
