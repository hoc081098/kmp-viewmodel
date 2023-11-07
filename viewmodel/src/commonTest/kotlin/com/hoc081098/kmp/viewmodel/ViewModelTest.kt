package com.hoc081098.kmp.viewmodel

import kotlin.coroutines.ContinuationInterceptor
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

class DemoViewModel : ViewModel {
  constructor() : super()

  constructor(closeables: List<Closeable>) : super(*closeables.toTypedArray())

  private val calls = Channel<Unit>(Channel.UNLIMITED)
  val scope get() = super.viewModelScope

  override fun onCleared() {
    super.onCleared()
    calls.trySend(Unit).getOrThrow()
  }

  suspend fun onClearedCount(): Int {
    calls.close()
    return calls.receiveAsFlow().count()
  }
}

@OptIn(InternalKmpViewModelApi::class)
private fun createDemoViewModel(): Pair<DemoViewModel, () -> Unit> {
  val viewModelStore = createViewModelStore()

  return DemoViewModel()
    .also { viewModelStore.put("DemoViewModel#${it.hashCode()}", it) } to
      viewModelStore::clear
}

@OptIn(ExperimentalStdlibApi::class)
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
  fun scopeMustHaveAJob() {
    val vm = DemoViewModel()
    assertNotNull(vm.scope.coroutineContext[Job])
  }

  @Test
  fun scopeMustHaveAMainDispatcher() {
    val vm = DemoViewModel()
    assertIs<MainCoroutineDispatcher>(
      vm.scope.coroutineContext[CoroutineDispatcher],
    )

    println(vm.scope.coroutineContext[CoroutineDispatcher])
  }

  @Test
  fun addCloseablesThatWillBeClosedWhenClear() = runTest {
    val (vm, clear) = createDemoViewModel()

    val closeables = List(100) {
      object : Closeable {
        var closed = false
        override fun close() {
          closed = true
        }
      }
    }

    closeables.forEach(vm::addCloseable)
    clear()

    closeables.forEach { assertTrue { it.closed } }
  }

  //
  //  @Test
  //  fun constructor_addCloseablesThatWillBeClosedWhenClear() = runTest {
  //    val closeables = List(100) {
  //      object : Closeable {
  //        var closed = AtomicBoolean(false)
  //        override fun close() {
  //          closed.value = true
  //        }
  //      }
  //    }
  //
  //    val vm = DemoViewModel(closeables)
  //    delay(1)
  //    vm.clear()
  //
  //    closeables.forEach { assertTrue { it.closed.value } }
  //  }
  //
  //  @Test
  //  fun addCloseablesThatWillBeClosedWhenClear_multiThreads() = runTest {
  //    val vm = DemoViewModel()
  //
  //    val closeables = List(100) {
  //      object : Closeable {
  //        var closed = AtomicBoolean(false)
  //        override fun close() {
  //          closed.value = true
  //        }
  //      }
  //    }
  //
  //    closeables
  //      .map { launch { runBlockInNewThread { vm.addCloseable(it) } } }
  //      .joinAll()
  //    (0..10)
  //      .map { launch { runBlockInNewThread(vm::clear) } }
  //      .joinAll()
  //
  //    closeables.forEach { assertTrue { it.closed.value } }
  //  }
  //
  //  @Test
  //  fun cannotAddCloseableAfterClear() = runTest {
  //    val vm = DemoViewModel()
  //    vm.clear()
  //
  //    val closeable = Closeable { error("Cannot reach here!") }
  //
  //    assertFailsWith<IllegalStateException> {
  //      vm.addCloseable(closeable)
  //    }
  //  }
  //
  //  @Test
  //  fun cannotAddCloseableAfterClear_multiThreads() = runTest {
  //    val vm = DemoViewModel()
  //    val isCleared = AtomicBoolean(false)
  //
  //    repeat(10) {
  //      launch {
  //        runBlockInNewThread {
  //          vm.clear()
  //        }
  //        isCleared.value = true
  //      }
  //    }
  //
  //    delay(10)
  //
  //    (0..1000)
  //      .map {
  //        launch {
  //          runBlockInNewThread {
  //            if (isCleared.value) {
  //              assertFailsWith<IllegalStateException> { vm.addCloseable { error("Cannot reach here!") } }
  //            }
  //          }
  //        }
  //      }
  //      .joinAll()
  //  }
}
