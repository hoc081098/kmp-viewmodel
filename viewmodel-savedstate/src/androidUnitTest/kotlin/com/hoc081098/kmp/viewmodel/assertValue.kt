package com.hoc081098.kmp.viewmodel

import androidx.lifecycle.LiveData
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@MainThread
internal fun <T : Any?> LiveData<T>.assertValue(expected: T?) {
  var received = false
  observeForever {
    received = true
    assertEquals(expected, it)
  }
  assertTrue { received }
}

@MainThread
internal fun <T : Any?> LiveData<T>.assertValues(vararg expected: T?) {
  var received = false
  var index = 0
  observeForever {
    received = true
    assertEquals(expected[index++], it)
  }
  assertTrue { received }
}
