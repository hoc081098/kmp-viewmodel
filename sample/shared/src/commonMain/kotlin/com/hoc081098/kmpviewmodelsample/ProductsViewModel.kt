package com.hoc081098.kmpviewmodelsample

import com.hoc081098.flowext.flatMapFirst
import com.hoc081098.flowext.flowFromSuspend
import com.hoc081098.flowext.interval
import com.hoc081098.flowext.startWith
import com.hoc081098.kmp.viewmodel.Closeable
import com.hoc081098.kmp.viewmodel.ViewModel
import io.github.aakira.napier.Napier
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.channels.Channel
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
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn

data class ProductsState(
  val products: List<ProductItem>,
  val isLoading: Boolean,
  val error: Throwable?,
  val isRefreshing: Boolean,
) {
  companion object {
    val INITIAL = ProductsState(
      products = emptyList(),
      isLoading = true,
      error = null,
      isRefreshing = false,
    )
  }
}

sealed interface ProductSingleEvent {
  sealed interface Refresh : ProductSingleEvent {
    object Success : Refresh
    data class Failure(val error: Throwable) : Refresh
  }
}

sealed interface ProductsAction {
  object Load : ProductsAction
  object Refresh : ProductsAction
}

private fun interface Reducer {
  operator fun invoke(state: ProductsState): ProductsState
}

class ProductsViewModel(
  private val getProducts: GetProducts,
) : ViewModel(
  Closeable { Napier.d("[DEMO] Closable 1 ...") },
  Closeable { Napier.d("[DEMO] Closable 2 ...") },
) {
  private val _eventChannel = Channel<ProductSingleEvent>(Int.MAX_VALUE)
  private val _actionFlow = MutableSharedFlow<ProductsAction>(Int.MAX_VALUE)

  val stateFlow: StateFlow<ProductsState>
  val eventFlow: Flow<ProductSingleEvent> = _eventChannel.receiveAsFlow()

  init {
    addCloseable { Napier.d("[DEMO] Closable 3 ...") }
    addCloseable(_eventChannel::close)

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
  }

  //region Handlers
  private fun Flow<ProductsAction.Refresh>.refreshFlow(): Flow<Reducer> =
    flatMapFirst {
      flowFromSuspend { getProducts() }
        .map { products ->
          _eventChannel.trySend(ProductSingleEvent.Refresh.Success)

          Reducer {
            it.copy(
              products = products,
              isRefreshing = false,
              error = null,
            )
          }
        }
        .catch {
          _eventChannel.trySend(ProductSingleEvent.Refresh.Failure(it))
          emit(Reducer { it.copy(isRefreshing = false) })
        }
        .startWith { Reducer { it.copy(isRefreshing = true) } }
    }

  private fun Flow<ProductsAction.Load>.loadFlow(): Flow<Reducer> =
    flatMapLatest {
      flowFromSuspend { getProducts() }
        .map { products ->
          Reducer {
            it.copy(
              products = products,
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
