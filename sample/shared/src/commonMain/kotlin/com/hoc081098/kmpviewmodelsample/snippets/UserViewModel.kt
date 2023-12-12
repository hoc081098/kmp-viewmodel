@file:Suppress("unused") // Snippet

package com.hoc081098.kmpviewmodelsample.snippets

import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.SavedStateHandleKey
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.getStateFlow
import com.hoc081098.kmp.viewmodel.parcelable.Parcelable
import com.hoc081098.kmp.viewmodel.parcelable.Parcelize
import com.hoc081098.kmp.viewmodel.set
import com.hoc081098.kmp.viewmodel.wrapper.wrap
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

@Parcelize
data class User(
  val id: Long,
  val name: String,
) : Parcelable

class UserViewModel(
  private val savedStateHandle: SavedStateHandle,
  private val getUserUseCase: suspend () -> User?,
) : ViewModel() {
  val userStateFlow = savedStateHandle.getStateFlow(USER_KEY).wrap()

  fun getUser() {
    viewModelScope.launch {
      try {
        val user = getUserUseCase()
        savedStateHandle[USER_KEY] = user
      } catch (e: CancellationException) {
        throw e
      } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
        e.printStackTrace()
      }
    }
  }

  private companion object {
    private val USER_KEY = SavedStateHandleKey<User?>("user_key", null)
  }
}
