package com.hoc081098.kmp.viewmodel.internal

import java.util.concurrent.atomic.AtomicBoolean as JavaAtomicBoolean

internal actual class AtomicBoolean actual constructor(value: Boolean) {
  private val atomic = JavaAtomicBoolean(value)

  actual var value: Boolean
    get() = atomic.get()
    set(value) = atomic.set(value)

  actual fun compareAndSet(expectedValue: Boolean, newValue: Boolean): Boolean =
    atomic.compareAndSet(expectedValue, newValue)
}