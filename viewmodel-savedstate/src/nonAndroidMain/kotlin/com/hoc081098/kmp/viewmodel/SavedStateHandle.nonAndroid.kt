package com.hoc081098.kmp.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

public actual class SavedStateHandle {
  private val regular = mutableMapOf<String, Any?>()
  private val flows = mutableMapOf<String, MutableStateFlow<Any?>>()

  public actual constructor(initialState: Map<String, Any?>) {
    regular.putAll(initialState)
  }

  public actual constructor()

  @MainThread
  public actual operator fun contains(key: String): Boolean = regular.containsKey(key)

  @MainThread
  public actual operator fun <T> get(key: String): T? {
    @Suppress("UNCHECKED_CAST")
    return regular[key] as T?
  }

  @MainThread
  public actual fun <T> getStateFlow(key: String, initialValue: T): StateFlow<T> {
    @Suppress("UNCHECKED_CAST")
    // If a flow exists we should just return it, and since it is a StateFlow and a value must
    // always be set, we know a value must already be available
    return flows.getOrPut(key) {
      // If there is not a value associated with the key, add the initial value, otherwise,
      // use the one we already have.
      if (!regular.containsKey(key)) {
        regular[key] = initialValue
      }
      MutableStateFlow(regular[key]).apply { flows[key] = this }
    }.asStateFlow() as StateFlow<T>
  }

  @MainThread
  public actual fun keys(): Set<String> = regular.keys

  @MainThread
  public actual fun <T> remove(key: String): T? {
    @Suppress("UNCHECKED_CAST")
    val latestValue = regular.remove(key) as T?
    flows.remove(key)
    return latestValue
  }

  @MainThread
  public actual operator fun <T> set(key: String, value: T?) {
    regular[key] = value
    flows[key]?.value = value
  }
}
