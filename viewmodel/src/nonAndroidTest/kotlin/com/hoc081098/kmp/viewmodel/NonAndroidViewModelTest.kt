package com.hoc081098.kmp.viewmodel

import com.hoc081098.kmp.viewmodel.internal.AtomicBoolean
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue
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
    val vm = DemoViewModel()

    val deferred = CompletableDeferred<Throwable?>()
    vm.scope.coroutineContext[Job]!!.invokeOnCompletion(deferred::complete)

    vm.clear()
    vm.clear()
    vm.clear()

    assertIs<CancellationException>(deferred.await())
  }

  @Test
  fun onClearedWillBeCalledOnce() = runTest {
    val vm = DemoViewModel()

    vm.clear()
    vm.clear()
    vm.clear()

    assertEquals<Int>(1, vm.onClearedCount())
  }

  @Test
  fun onClearedWillBeCalledOnce_multiThreads() = runTest {
    val vm = DemoViewModel()

    (0..10)
      .map { launch { runBlockInNewThread(vm::clear) } }
      .joinAll()

    assertEquals<Int>(1, vm.onClearedCount())
  }

  @Test
  fun addCloseablesThatWillBeClosedWhenClear() = runTest {
    val vm = DemoViewModel()

    val closeables = List(100) {
      object : Closeable {
        var closed = AtomicBoolean(false)
        override fun close() {
          closed.value = true
        }
      }
    }

    closeables.forEach(vm::addCloseable)
    vm.clear()

    closeables.forEach { assertTrue { it.closed.value } }
  }

  @Test
  fun constructor_addCloseablesThatWillBeClosedWhenClear() = runTest {
    val closeables = List(100) {
      object : Closeable {
        var closed = AtomicBoolean(false)
        override fun close() {
          closed.value = true
        }
      }
    }

    val vm = DemoViewModel(closeables)
    delay(1)
    vm.clear()

    closeables.forEach { assertTrue { it.closed.value } }
  }

  @Test
  fun addCloseablesThatWillBeClosedWhenClear_multiThreads() = runTest {
    val vm = DemoViewModel()

    val closeables = List(100) {
      object : Closeable {
        var closed = AtomicBoolean(false)
        override fun close() {
          closed.value = true
        }
      }
    }

    closeables
      .map { launch { runBlockInNewThread { vm.addCloseable(it) } } }
      .joinAll()
    (0..10)
      .map { launch { runBlockInNewThread(vm::clear) } }
      .joinAll()

    closeables.forEach { assertTrue { it.closed.value } }
  }

  @Test
  fun cannotAddCloseableAfterClear() = runTest {
    val vm = DemoViewModel()
    vm.clear()

    val closeable = Closeable { error("Cannot reach here!") }

    assertFailsWith<IllegalStateException> {
      vm.addCloseable(closeable)
    }
  }

  @Test
  fun cannotAddCloseableAfterClear_multiThreads() = runTest {
    val vm = DemoViewModel()
    val isCleared = AtomicBoolean(false)

    repeat(10) {
      launch {
        runBlockInNewThread {
          vm.clear()
        }
        isCleared.value = true
      }
    }

    delay(10)

    (0..1000)
      .map {
        launch {
          runBlockInNewThread {
            if (isCleared.value) {
              assertFailsWith<IllegalStateException> { vm.addCloseable { error("Cannot reach here!") } }
            }
          }
        }
      }
      .joinAll()
  }

  @Test
  fun cannotAccessScopeAfterClear() = runTest {
    val vm = DemoViewModel()
    vm.clear()

    assertFailsWith<IllegalStateException> { vm.scope }
  }

  @Test
  fun cannotAccessScopeAfterClear_multiThreads() = runTest {
    val vm = DemoViewModel()
    val isCleared = AtomicBoolean(false)

    repeat(10) {
      launch {
        runBlockInNewThread {
          vm.clear()
        }
        isCleared.value = true
      }
    }

    delay(10)

    (0..1000)
      .map {
        launch {
          runBlockInNewThread {
            if (isCleared.value) {
              assertFailsWith<IllegalStateException> { vm.scope }
            }
          }
        }
      }
      .joinAll()
  }
}
