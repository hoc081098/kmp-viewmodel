package com.hoc081098.kmp.viewmodel

import kotlin.coroutines.ContinuationInterceptor
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

class DemoViewModel : ViewModel() {
  val scope get() = super.viewModelScope
}

class ViewModelTest {
  @BeforeTest
  fun setup() {
    Dispatchers.setMain(StandardTestDispatcher())
  }

  @AfterTest
  fun teardown() {
    Dispatchers.resetMain()
  }

  @Test
  fun createAndAccessScope() = runTest {
    val vm = DemoViewModel()
    vm.scope
      .launch { delay(100) }
      .join()
  }

  @Test
  fun scopeMustHasAJob() {
    val vm = DemoViewModel()
    assertNotNull(vm.scope.coroutineContext[Job])
  }

  @Test
  fun scopeMustHasAMainDispatcher() {
    val vm = DemoViewModel()
    assertIs<MainCoroutineDispatcher>(
      vm.scope.coroutineContext[ContinuationInterceptor]
    )
  }
}
