package com.hoc081098.kmp.viewmodel

import kotlinx.coroutines.flow.StateFlow

public expect class SavedStateHandle {
  @Suppress("UnusedPrivateMember")
  public constructor(initialState: Map<String, Any?>)
  public constructor()

  public operator fun contains(key: String): Boolean
  public operator fun <T> get(key: String): T?

  public fun <T> getStateFlow(key: String, initialValue: T): StateFlow<T>
  public fun keys(): Set<String>

  public fun <T> remove(key: String): T?
  public operator fun <T> set(key: String, value: T?): Unit
}
