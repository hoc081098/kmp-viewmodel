package com.hoc081098.kmp.viewmodel

/**
 * Responsible for creating a new [SavedStateHandle] instance.
 * This is used to override the default [SavedStateHandle] creation logic.
 *
 * @see SAVED_STATE_HANDLE_FACTORY_KEY
 * @see createSavedStateHandle
 */
public fun interface SavedStateHandleFactory {
  public fun create(): SavedStateHandle
}
