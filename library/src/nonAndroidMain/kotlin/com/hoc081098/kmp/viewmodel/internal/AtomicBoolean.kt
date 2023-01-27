package com.hoc081098.kmp.viewmodel.internal

internal expect class AtomicBoolean(value: Boolean = false) {
  fun compareAndSet(expectedValue: Boolean, newValue: Boolean): Boolean
  var value: Boolean
}
