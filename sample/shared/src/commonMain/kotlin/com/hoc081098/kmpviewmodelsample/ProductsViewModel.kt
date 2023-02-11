package com.hoc081098.kmpviewmodelsample

import com.hoc081098.flowext.flatMapFirst
import com.hoc081098.flowext.flowFromSuspend
import com.hoc081098.flowext.startWith
import com.hoc081098.kmp.viewmodel.Closeable
import com.hoc081098.kmp.viewmodel.ViewModel
import io.github.aakira.napier.Napier
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
  Closeable { Napier.d("Closable 1 ...") },
  Closeable { Napier.d("Closable 2 ...") },
) {
  private val _action = MutableSharedFlow<ProductsAction>(Int.MAX_VALUE)

  val stateFlow: StateFlow<ProductsState>

  init {
    stateFlow = merge(
      _action
        .filterIsInstance<ProductsAction.Load>()
        .loadFlow(),
      _action
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
          Reducer {
            it.copy(
              products = products,
              isRefreshing = false,
              error = null,
            )
          }
        }
        .catch { Reducer { it.copy(isRefreshing = false) } }
        .startWith { Reducer { it.copy(isRefreshing = false) } }
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
          Reducer {
            it.copy(
              isLoading = false,
              error = error,
            )
          }
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
    _action.tryEmit(action)
  }
}
