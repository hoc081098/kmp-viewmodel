package com.hoc081098.kmp.viewmodel.wrapper

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest

class NullableStateFlowWrapperTest {
  @Test
  fun basic() = runTest {
    val stateFlow = MutableStateFlow<Int?>(1)
    val flowWrapper = stateFlow.wrap()

    val values = mutableListOf<Int?>()
    val job = launch(context = UnconfinedTestDispatcher(testScheduler)) {
      flowWrapper.toList(values)
    }

    stateFlow.value = 2
    stateFlow.value = null
    stateFlow.value = 4

    job.cancelAndJoin()
    assertEquals(expected = listOf(1, 2, null, 4), actual = values)
  }
}
