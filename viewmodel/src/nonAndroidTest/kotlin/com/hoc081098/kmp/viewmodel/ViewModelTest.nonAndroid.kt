package com.hoc081098.kmp.viewmodel

import com.hoc081098.kmp.viewmodel.utils.TestAtomicBoolean
import com.hoc081098.kmp.viewmodel.utils.delegated
import com.hoc081098.kmp.viewmodel.utils.runBlockInNewThread
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

class NonAndroidViewModelTest {
  @BeforeTest
  fun setup() {
    Dispatchers.setMain(StandardTestDispatcher())
  }

  @AfterTest
  fun teardown() {
    Dispatchers.resetMain()
  }

  @Test
  fun callClearWillCancelScope() = runTest {
    val vm = TestViewModel()

    val deferred = CompletableDeferred<Throwable?>()
    vm.scope.coroutineContext[Job]!!.invokeOnCompletion(deferred::complete)

    vm.clear()
    vm.clear()
    vm.clear()

    assertIs<CancellationException>(deferred.await())
  }

  @Test
  fun onClearedWillBeCalledOnce() = runTest {
    val vm = TestViewModel()

    vm.clear()
    vm.clear()
    vm.clear()

    assertEquals(1, vm.onClearedCount())
  }

  @Test
  fun onClearedWillBeCalledOnce_multiThreads() = runTest {
    val vm = TestViewModel()

    (0..10)
      .map { launch { runBlockInNewThread(vm::clear) } }
      .joinAll()

    assertEquals(1, vm.onClearedCount())
  }

  @Test
  fun cannotAccessScopeAfterClear() = runTest {
    val vm = TestViewModel()
    vm.clear()

    assertFailsWith<IllegalStateException> { vm.scope }
  }

  @Test
  fun cannotAccessScopeAfterClear_multiThreads() = runTest {
    val vm = TestViewModel()
    var isCleared by TestAtomicBoolean().delegated()

    repeat(10) {
      launch {
        runBlockInNewThread {
          vm.clear()
        }
        isCleared = true
      }
    }

    delay(10)

    (0..1000)
      .map {
        launch {
          runBlockInNewThread {
            if (isCleared) {
              assertFailsWith<IllegalStateException> { vm.scope }
            }
          }
        }
      }
      .joinAll()
  }
}
