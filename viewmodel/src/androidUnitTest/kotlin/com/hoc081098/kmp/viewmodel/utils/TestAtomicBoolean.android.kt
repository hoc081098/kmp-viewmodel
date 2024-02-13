package com.hoc081098.kmp.viewmodel.utils

import java.util.concurrent.atomic.AtomicBoolean

internal actual class TestAtomicBoolean actual constructor(value: Boolean) {
  private val ref = AtomicBoolean(value)

  actual var value: Boolean
    get() = ref.get()
    set(value) = ref.set(value)
}
