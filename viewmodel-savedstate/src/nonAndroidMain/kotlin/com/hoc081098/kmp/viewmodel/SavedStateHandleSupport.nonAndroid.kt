package com.hoc081098.kmp.viewmodel

@MainThread
public actual fun CreationExtras.createSavedStateHandle(): SavedStateHandle =
  this[SAVED_STATE_HANDLE_FACTORY_KEY]?.create() ?: SavedStateHandle()
