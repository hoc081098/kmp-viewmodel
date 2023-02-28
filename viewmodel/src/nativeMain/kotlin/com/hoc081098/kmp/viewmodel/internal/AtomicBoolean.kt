package com.hoc081098.kmp.viewmodel.internal

import kotlin.native.concurrent.AtomicInt

internal actual class AtomicBoolean actual constructor(value: Boolean) {
  private val atomic = AtomicInt(value.asInt)

  actual var value: Boolean
    get() = atomic.value != 0
    set(value) {
      atomic.value = value.asInt
    }

  actual fun compareAndSet(expectedValue: Boolean, newValue: Boolean): Boolean =
    atomic.compareAndSet(expectedValue.asInt, newValue.asInt)
}

private inline val Boolean.asInt: Int get() = if (this) 1 else 0
