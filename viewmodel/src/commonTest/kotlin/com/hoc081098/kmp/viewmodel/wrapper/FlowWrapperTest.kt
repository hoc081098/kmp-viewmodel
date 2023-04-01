package com.hoc081098.kmp.viewmodel.wrapper

import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertSame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

class FlowWrapperTest {
  @Test
  fun correctReturnTypes() = runTest {
    assertIs<NonNullFlowWrapper<Int>>(flowOf(1, 2, 3).wrap())
    assertIs<NonNullStateFlowWrapper<Int>>(MutableStateFlow(1).wrap())

    assertIs<NullableFlowWrapper<Int?>>(flowOf(1, 2, 3, null).wrap())
    assertIs<NullableStateFlowWrapper<Int>>(MutableStateFlow<Int?>(null).wrap())
  }

  @Test
  fun returnItselfIfPossible() = runTest {
    flowOf(1, 2, 3).wrap().let { wrapper ->
      assertSame(wrapper, wrapper.wrap())
    }
    flowOf(1, 2, 3, null).wrap().let { wrapper ->
      assertSame(wrapper, wrapper.wrap())
    }

    MutableStateFlow(1).wrap().let { wrapper ->
      assertSame(wrapper, wrapper.wrap())
    }
    MutableStateFlow<Int?>(null).wrap().let { wrapper ->
      assertSame(wrapper, wrapper.wrap())
    }
  }
}
