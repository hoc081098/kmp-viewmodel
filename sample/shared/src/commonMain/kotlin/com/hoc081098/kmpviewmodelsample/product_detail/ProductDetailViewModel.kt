@file:Suppress("PackageNaming")

package com.hoc081098.kmpviewmodelsample.product_detail

import com.hoc081098.flowext.flatMapFirst
import com.hoc081098.flowext.flowFromSuspend
import com.hoc081098.flowext.startWith
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.wrapper.NonNullStateFlowWrapper
import com.hoc081098.kmp.viewmodel.wrapper.wrap
import com.hoc081098.kmpviewmodelsample.Immutable
import com.hoc081098.kmpviewmodelsample.ProductItemUi
import com.hoc081098.kmpviewmodelsample.toProductItemUi
import io.github.aakira.napier.Napier
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

@Immutable
sealed interface ProductDetailState {
  data class Success(val product: ProductItemUi) : ProductDetailState
  object Loading : ProductDetailState
  data class Error(val error: Throwable) : ProductDetailState
}

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailViewModel(
  savedStateHandle: SavedStateHandle,
  private val getProductById: GetProductById,
) : ViewModel() {
  val id = checkNotNull(savedStateHandle.get<Int>(ID_KEY)) {
    """id must not be null.
      |For non-Android platforms, you must set id to `SavedStateHandle` with key $ID_KEY,
      |and pass that `SavedStateHandle` to `ProductDetailViewModel` constructor.
      |
    """.trimMargin()
  }
  private val refreshFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
  private val retryFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
  private val productItemFlow = flowFromSuspend { getProductById(id) }
    .onStart { Napier.d("getProductById id=$id") }
    .map { ProductDetailState.Success(it.toProductItemUi()) }

  val stateFlow: NonNullStateFlowWrapper<ProductDetailState> = merge(
    // initial load & retry
    retryFlow
      .startWith(Unit)
      .flatMapLatest {
        productItemFlow
          .startWith(ProductDetailState.Loading)
          .catch { emit(ProductDetailState.Error(it)) }
      },
    // refresh flow
    refreshFlow
      .flatMapFirst {
        // doesn't show loading state and error state when refreshing
        productItemFlow.catch { }
      },
  )
    .onEach { Napier.d("stateFlow: ${it::class.simpleName}") }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.Lazily,
      initialValue = ProductDetailState.Loading,
    )
    .wrap()

  fun refresh() {
    viewModelScope.launch {
      yield()

      if (stateFlow.value is ProductDetailState.Success) {
        Napier.d("refresh")
        refreshFlow.emit(Unit)
      }
    }
  }

  fun retry() {
    viewModelScope.launch {
      if (stateFlow.value is ProductDetailState.Error) {
        Napier.d("retry")
        retryFlow.emit(Unit)
      }
    }
  }

  companion object {
    // This key is used by non-Android platforms to set id to SavedStateHandle,
    // used by Android platform to set id to Bundle (handled by Compose-Navigation).
    const val ID_KEY = "id"

    /**
     * This factory method is used by non-Android platforms to create an instance of [ProductDetailViewModel].
     */
    fun create(id: Int, getProductById: GetProductById): ProductDetailViewModel = ProductDetailViewModel(
      savedStateHandle = SavedStateHandle(mapOf(ID_KEY to id)),
      getProductById = getProductById,
    )
  }
}
