@file:Suppress("unused") // Snippet

package com.hoc081098.kmpviewmodelsample.snippets

import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.safe.NonNullSavedStateHandleKey
import com.hoc081098.kmp.viewmodel.safe.safe
import com.hoc081098.kmp.viewmodel.safe.string
import com.hoc081098.kmp.viewmodel.wrapper.NonNullStateFlowWrapper
import com.hoc081098.kmp.viewmodel.wrapper.wrap

class SearchViewModel(
  private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
  internal val searchTermStateFlow: NonNullStateFlowWrapper<String> = savedStateHandle
    .safe { it.getStateFlow(searchTermKey) }
    .wrap()

  internal fun changeSearchTerm(searchTerm: String) {
    savedStateHandle.safe { it[searchTermKey] = searchTerm }
  }

  companion object {
    val searchTermKey: NonNullSavedStateHandleKey<String> = NonNullSavedStateHandleKey.string(
      key = "searchTerm",
      defaultValue = ""
    )
  }
}
