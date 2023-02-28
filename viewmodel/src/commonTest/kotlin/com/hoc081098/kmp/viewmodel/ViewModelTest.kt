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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

class DemoViewModel : ViewModel() {
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
      vm.scope.coroutineContext[ContinuationInterceptor],
    )
  }
}
