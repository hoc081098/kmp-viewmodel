package com.hoc081098.kmpviewmodelsample

import com.hoc081098.flowext.flowFromSuspend
import com.hoc081098.flowext.startWith
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModel
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class SearchProductsState(
  val products: List<ProductItem>,
  val isLoading: Boolean,
  val error: Throwable?,
  val term: String?,
) {
  companion object {
    val INITIAL = SearchProductsState(
      products = emptyList(),
      isLoading = false,
      error = null,
      term = null,
    )
  }
}

class SearchProductsViewModel(
  private val searchProducts: SearchProducts,
  private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
  private val searchTermStateFlow =
    savedStateHandle.getStateFlow<String?>(SEARCH_TERM_KEY, null)

  val stateFlow: StateFlow<SearchProductsState> = searchTermStateFlow
    .debounce(400.milliseconds)
    .map { it.orEmpty().trim() }
    .distinctUntilChanged()
    .flatMapLatest(searchProducts::executeSearching)
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.Lazily,
      initialValue = SearchProductsState.INITIAL,
    )

  fun search(term: String) {
    savedStateHandle[SEARCH_TERM_KEY] = term
  }

  companion object {
    const val SEARCH_TERM_KEY = "com.hoc081098.kmpviewmodelsample.search_term"
  }
}

private fun SearchProducts.executeSearching(term: String): Flow<SearchProductsState> {
  return flowFromSuspend { if (term.isEmpty()) emptyList() else invoke(term) }
    .map { products ->
      SearchProductsState(
        products = products,
        isLoading = false,
        error = null,
        term = term,
      )
    }
    .startWith {
      SearchProductsState(
        isLoading = true,
        error = null,
        term = term,
        products = emptyList(),
      )
    }
    .catch { error ->
      SearchProductsState(
        isLoading = false,
        error = error,
        term = term,
        products = emptyList(),
      )
    }
}
