package com.hoc081098.kmp.viewmodel.utils

import com.hoc081098.kmp.viewmodel.Closeable
import com.hoc081098.kmp.viewmodel.utils.TestAtomicBoolean
import com.hoc081098.kmp.viewmodel.utils.delegated

internal class TestCloseable : Closeable {
  var closed by TestAtomicBoolean().delegated()
    private set

  override fun close() {
    check(!closed) { "Already closed!" }
    closed = true
  }
}
