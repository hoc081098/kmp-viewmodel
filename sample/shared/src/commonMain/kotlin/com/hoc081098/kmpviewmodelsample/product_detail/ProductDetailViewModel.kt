package com.hoc081098.kmpviewmodelsample.product_detail

import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModel
import kotlinx.coroutines.flow.StateFlow

class ProductDetailViewModel(
  savedStateHandle: SavedStateHandle,
) : ViewModel() {
  val idStateFlow: StateFlow<Int?> = savedStateHandle.getStateFlow<Int?>(ID_KEY, null)

  companion object {
    const val ID_KEY = "id"
  }
}
