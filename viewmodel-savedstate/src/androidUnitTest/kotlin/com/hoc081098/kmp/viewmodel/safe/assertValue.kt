package com.hoc081098.kmp.viewmodel.safe

import androidx.lifecycle.LiveData
import com.hoc081098.kmp.viewmodel.MainThread
import com.hoc081098.kmp.viewmodel.extendedAssertEquals
import kotlin.test.assertTrue

@MainThread
internal fun <T : Any?> LiveData<T>.assertValue(expected: T?) {
  var received = false
  observeForever {
    received = true
    extendedAssertEquals(expected, it)
  }
  assertTrue { received }
}

@MainThread
internal fun <T : Any?> LiveData<T>.assertValues(vararg expected: T?) {
  var received = false
  var index = 0
  observeForever {
    received = true
    extendedAssertEquals(expected[index++], it)
  }
  assertTrue { received }
}
