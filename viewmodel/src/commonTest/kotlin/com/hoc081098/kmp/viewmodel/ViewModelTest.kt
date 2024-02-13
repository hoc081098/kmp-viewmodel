package com.hoc081098.kmp.viewmodel

import com.hoc081098.kmp.viewmodel.utils.TestAtomicBoolean
import com.hoc081098.kmp.viewmodel.utils.TestCloseable
import com.hoc081098.kmp.viewmodel.utils.delegated
import com.hoc081098.kmp.viewmodel.utils.runBlockInNewThread
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
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

class TestViewModel : ViewModel {
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
private fun createTestViewModel(closeable: List<Closeable> = emptyList()): Pair<TestViewModel, () -> Unit> {
  val viewModelStore = createViewModelStore()

  return TestViewModel(closeable)
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
    val vm = TestViewModel()
    vm.scope
      .launch { delay(100) }
      .join()
  }

  @Test
  fun scopeMustHaveAJob() {
    val vm = TestViewModel()
    assertNotNull(vm.scope.coroutineContext[Job])
  }

  @Test
  fun scopeMustHaveAMainDispatcher() {
    val vm = TestViewModel()
    assertIs<MainCoroutineDispatcher>(
      vm.scope.coroutineContext[CoroutineDispatcher],
    )

    println(vm.scope.coroutineContext[CoroutineDispatcher])
  }

  @Test
  fun addCloseablesThatWillBeClosedWhenClear() = runTest {
    val (vm, clear) = createTestViewModel()

    val closeables = List(100) { TestCloseable() }

    closeables.forEach(vm::addCloseable)
    clear()

    closeables.forEach { assertTrue { it.closed } }
  }

  @Test
  fun constructor_addCloseablesThatWillBeClosedWhenClear() = runTest {
    val closeables = List(100) { TestCloseable() }

    val (_, clear) = createTestViewModel(closeables)
    delay(1)
    clear()

    closeables.forEach { assertTrue { it.closed } }
  }

  @Test
  fun addCloseablesThatWillBeClosedWhenClear_multiThreads() = runTest {
    val (vm, clear) = createTestViewModel()

    val closeables = List(100) { TestCloseable() }

    closeables
      .map { launch { runBlockInNewThread { vm.addCloseable(it) } } }
      .joinAll()
    (0..10)
      .map { launch { runBlockInNewThread(clear) } }
      .joinAll()

    closeables.forEach { assertTrue { it.closed } }
  }

  @Test
  fun addCloseableAfterCleared() = runTest {
    val (vm, clear) = createTestViewModel()
    clear()

    val closeable = TestCloseable()
    vm.addCloseable(closeable)

    assertTrue { closeable.closed }
  }

  @Test
  fun addCloseableAfterCleared_multiThreads() = runTest {
    val (vm, clear) = createTestViewModel()
    var isCleared by TestAtomicBoolean().delegated()

    repeat(10) {
      launch {
        runBlockInNewThread { clear() }
        isCleared = true
      }
    }

    delay(10)

    (0..1000)
      .map {
        launch {
          runBlockInNewThread {
            if (isCleared) {
              val closeable = TestCloseable()
              vm.addCloseable(closeable)
              assertTrue { closeable.closed }
            }
          }
        }
      }
      .joinAll()
  }
}
