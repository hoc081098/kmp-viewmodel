package com.hoc081098.kmp.viewmodel

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

expect fun createViewModelStore(): ViewModelStore

// Copied from https://github.com/androidx/androidx/blob/37df1f7745e04a9c7e2a7fb60f7449491276916f/lifecycle/lifecycle-viewmodel/src/test/java/androidx/lifecycle/ViewModelStoreTest.kt#L29
class ViewModelStoreTest {
  @BeforeTest
  fun setup() {
    Dispatchers.setMain(StandardTestDispatcher())
  }

  @AfterTest
  fun teardown() {
    Dispatchers.resetMain()
  }

  @OptIn(InternalKmpViewModelApi::class)
  @Test
  fun testClear() = runTest {
    val store = createViewModelStore()
    val viewModel1 = TestViewModel()
    val viewModel2 = TestViewModel()

    store.put("a", viewModel1)
    store.put("b", viewModel2)

    assertFalse(viewModel1.cleared)
    assertFalse(viewModel2.cleared)
    assertSame(viewModel1, store["a"])
    assertSame(viewModel2, store["b"])

    store.clear()

    assertTrue(viewModel1.cleared)
    assertTrue(viewModel2.cleared)
    assertNull(store["a"])
    assertNull(store["b"])
  }

  internal class TestViewModel : ViewModel() {
    var cleared = false
    public override fun onCleared() {
      cleared = true
    }
  }
}
