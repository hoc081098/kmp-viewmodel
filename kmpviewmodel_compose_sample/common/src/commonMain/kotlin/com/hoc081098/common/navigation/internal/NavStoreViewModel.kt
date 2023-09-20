package com.hoc081098.common.navigation.internal

import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.SavedStateHandleFactory
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelStore
import com.hoc081098.kmp.viewmodel.ViewModelStoreOwner

internal class DefaultViewModelStoreOwner(
  override val viewModelStore: ViewModelStore
) : ViewModelStoreOwner

internal expect fun createViewModelStore(): ViewModelStore

internal expect fun createSavedStateHandle(
  id: String,
  globalSavedStateHandle: SavedStateHandle,
): SavedStateHandle

internal class NavStoreViewModel(
  private val globalSavedStateHandle: SavedStateHandle,
) : ViewModel() {
  private val viewModelStoreOwners = mutableMapOf<String, DefaultViewModelStoreOwner>()
  private val savedStateHandles = mutableMapOf<String, SavedStateHandle>()
  private val savedStateHandleFactories = mutableMapOf<String, SavedStateHandleFactory>()

  internal infix fun provideViewModelStoreOwner(id: String): ViewModelStoreOwner = viewModelStoreOwners
    .getOrPut(id) {
      DefaultViewModelStoreOwner(
        viewModelStore = createViewModelStore()
      ).also { addCloseable(it.viewModelStore::clear) }
    }

  internal infix fun provideSavedStateHandleFactory(id: String): SavedStateHandleFactory =
    savedStateHandleFactories.getOrPut(id) {
      SavedStateHandleFactory { provideSavedStateHandle(id) }
    }

  private fun provideSavedStateHandle(id: String): SavedStateHandle = savedStateHandles.getOrPut(id) {
    createSavedStateHandle(
      id = id,
      globalSavedStateHandle = globalSavedStateHandle
    )
  }

  internal fun removeEntry(id: String) {
    val store = viewModelStoreOwners.remove(id)
    store?.viewModelStore?.clear()
  }
}
