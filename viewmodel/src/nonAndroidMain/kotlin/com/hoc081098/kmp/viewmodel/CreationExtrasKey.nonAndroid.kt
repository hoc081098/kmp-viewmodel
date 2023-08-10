package com.hoc081098.kmp.viewmodel

import kotlin.jvm.JvmField

public actual interface CreationExtrasKey<T>

public actual abstract class CreationExtras internal actual constructor() {
  internal val map: MutableMap<Key<*>, Any?> = mutableMapOf()

  public actual abstract operator fun <T> get(key: Key<T>): T?
}

public actual object EmptyCreationExtras : CreationExtras() {
  override fun <T> get(key: Key<T>): T? = null
}

public actual class MutableCreationExtras actual constructor(initialExtras: CreationExtras) : CreationExtras() {
  init {
    map.putAll(initialExtras.map)
  }

  /**
   * Associates the given [key] with [t]
   */
  public actual operator fun <T> set(key: Key<T>, t: T) {
    map[key] = t
  }

  public override fun <T> get(key: Key<T>): T? {
    @Suppress("UNCHECKED_CAST")
    return map[key] as T?
  }
}

@JvmField
public actual val VIEW_MODEL_KEY: Key<String> = object : Key<String> {}
