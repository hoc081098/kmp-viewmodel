package com.hoc081098.common.navigation.internal

import android.os.Bundle
import com.hoc081098.kmp.viewmodel.InternalKmpViewModelApi
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModelStore

@OptIn(InternalKmpViewModelApi::class)
internal actual fun createViewModelStore(): ViewModelStore =
  ViewModelStore(androidx.lifecycle.ViewModelStore())

internal actual fun createSavedStateHandle(id: String, globalSavedStateHandle: SavedStateHandle): SavedStateHandle {
  val restoredBundle = globalSavedStateHandle.get<Bundle>(id)

  return SavedStateHandle
    .createHandle(restoredBundle, null)
    .also { globalSavedStateHandle.setSavedStateProvider(id, it.savedStateProvider()) }
}
