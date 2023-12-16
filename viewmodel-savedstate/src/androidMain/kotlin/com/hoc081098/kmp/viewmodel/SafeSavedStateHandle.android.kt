package com.hoc081098.kmp.viewmodel

import androidx.lifecycle.LiveData
import com.hoc081098.kmp.viewmodel.safe.NullableSavedStateHandleKey
import com.hoc081098.kmp.viewmodel.safe.SafeSavedStateHandle
import com.hoc081098.kmp.viewmodel.safe.SavedStateHandleKey

public fun <T : Any> SafeSavedStateHandle.getLiveData(key: SavedStateHandleKey<T>): LiveData<T> =
  savedStateHandle
    .getLiveData(key.key, key.defaultValue)
    .also { check(it.isInitialized) { "LiveData isn't initialized" } }

public fun <T : Any> SafeSavedStateHandle.getLiveData(key: NullableSavedStateHandleKey<T>): LiveData<T?> =
  savedStateHandle
    .getLiveData(key.key, key.defaultValue)
    .also { check(it.isInitialized) { "LiveData isn't initialized" } }
