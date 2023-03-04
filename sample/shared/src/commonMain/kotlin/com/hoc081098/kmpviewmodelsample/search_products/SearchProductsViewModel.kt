@file:Suppress("PackageNaming")

package com.hoc081098.kmpviewmodelsample.search_products

import com.hoc081098.flowext.flowFromSuspend
import com.hoc081098.flowext.startWith
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.parcelable.Parcelable
import com.hoc081098.kmp.viewmodel.parcelable.Parcelize
import com.hoc081098.kmpviewmodelsample.ProductItem
import io.github.aakira.napier.Napier
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SearchProductsState(
  val products: List<ProductItem>,
  val isLoading: Boolean,
  val error: Throwable?,
  val submittedTerm: String?,
) {
  companion object {
    val INITIAL = SearchProductsState(
      products = emptyList(),
      isLoading = false,
      error = null,
      submittedTerm = null,
    )
  }
}

class SearchProductsViewModel(
  private val searchProducts: SearchProducts,
  private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
  val searchTermStateFlow =
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
    .onStart { Napier.d("search products term=$term") }
    .map { products ->
      SearchProductsState(
        products = products,
        isLoading = false,
        error = null,
        submittedTerm = term,
      )
    }
    .startWith {
      SearchProductsState(
        isLoading = true,
        error = null,
        submittedTerm = term,
        products = emptyList(),
      )
    }
    .catch { error ->
      Napier.e("search products failed", error)
      emit(
        SearchProductsState(
          isLoading = false,
          error = error,
          submittedTerm = term,
          products = emptyList(),
        ),
      )
    }
}

@Parcelize
data class User(
  val id: Long,
  val name: String
) : Parcelable

class ExampleViewModel(
  private val savedStateHandle: SavedStateHandle,
  private val getUserUseCase: suspend () -> User,
) : ViewModel() {
  val user = savedStateHandle.getStateFlow<User?>(USER_KEY, null)

  fun getUser() {
    viewModelScope.launch {
      try {
        savedStateHandle[USER_KEY] = getUserUseCase()
      } catch (e: CancellationException) {
        throw e
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }

  private companion object {
    private const val USER_KEY = "user_key"
  }
}
