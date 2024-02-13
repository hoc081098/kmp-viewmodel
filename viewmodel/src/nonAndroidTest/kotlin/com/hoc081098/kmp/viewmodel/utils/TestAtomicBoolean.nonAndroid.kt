package com.hoc081098.kmp.viewmodel.utils

internal actual class TestAtomicBoolean actual constructor(value: Boolean) {
  private val ref = com.hoc081098.kmp.viewmodel.internal.AtomicBoolean(value)
  actual var value: Boolean by ref::value
}
