package com.hoc081098.kmp.viewmodel.utils

import com.hoc081098.kmp.viewmodel.internal.AtomicBoolean

internal actual class TestAtomicBoolean actual constructor(value: Boolean) {
  private val ref = AtomicBoolean(value)
  actual var value: Boolean by ref::value
}
