package com.hoc081098.kmp.viewmodel

import com.hoc081098.kmp.viewmodel.internal.synchronized
import kotlin.test.Test
import kotlin.test.assertEquals

internal class LockableObjectTest : Lockable() {
  @Test
  fun testSync() {
    val result = synchronized(this) { bar() }
    assertEquals("OK", result)
  }

  private fun bar(): String =
    synchronized(this) {
      "OK"
    }
}
