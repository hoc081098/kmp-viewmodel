package com.hoc081098.kmp.viewmodel

import androidx.lifecycle.LiveData

public fun <T : Any> SafeSavedStateHandle.getLiveData(key: SavedStateHandleKey<T>): LiveData<T> =
  this.savedStateHandle
    .getLiveData(key.key, key.defaultValue)
    .also { check(it.isInitialized) { "LiveData isn't initialized" } }

public fun <T : Any> SafeSavedStateHandle.getLiveData(key: NullableSavedStateHandleKey<T>): LiveData<T?> =
  this.savedStateHandle
    .getLiveData(key.key, key.defaultValue)
    .also { check(it.isInitialized) { "LiveData isn't initialized" } }
