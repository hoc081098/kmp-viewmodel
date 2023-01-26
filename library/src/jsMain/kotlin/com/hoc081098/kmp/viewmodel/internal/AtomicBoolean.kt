package com.hoc081098.kmp.viewmodel.internal

internal actual class AtomicBoolean actual constructor(actual var value: Boolean) {
  actual fun compareAndSet(expectedValue: Boolean, newValue: Boolean): Boolean = if (value == expectedValue) {
    value = newValue
    true
  } else {
    false
  }
}