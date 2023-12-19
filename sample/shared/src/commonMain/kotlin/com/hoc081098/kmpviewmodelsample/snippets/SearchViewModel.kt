@file:Suppress("unused") // Snippet

package com.hoc081098.kmpviewmodelsample.snippets

import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.safe.NonNullSavedStateHandleKey
import com.hoc081098.kmp.viewmodel.safe.NullableSavedStateHandleKey
import com.hoc081098.kmp.viewmodel.safe.safe
import com.hoc081098.kmp.viewmodel.safe.string
import com.hoc081098.kmp.viewmodel.wrapper.NonNullStateFlowWrapper
import com.hoc081098.kmp.viewmodel.wrapper.NullableStateFlowWrapper
import com.hoc081098.kmp.viewmodel.wrapper.wrap

class SearchViewModel(
  private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
  internal val searchTermStateFlow: NonNullStateFlowWrapper<String> = savedStateHandle
    .safe { it.getStateFlow(searchTermKey) }
    .wrap()

  internal val userIdStateFlow: NullableStateFlowWrapper<String?> = savedStateHandle
    .safe { it.getStateFlow(userIdKey) }
    .wrap()

  internal fun changeSearchTerm(searchTerm: String) {
    savedStateHandle.safe { it[searchTermKey] = searchTerm }
  }

  private fun setUserId(userId: String?) = savedStateHandle.safe { it[userIdKey] = userId }

  companion object {
    private val searchTermKey: NonNullSavedStateHandleKey<String> = NonNullSavedStateHandleKey.string(
      key = "search_term",
      defaultValue = "",
    )
    private val userIdKey: NullableSavedStateHandleKey<String> = NullableSavedStateHandleKey.string("user_id")
  }
}
