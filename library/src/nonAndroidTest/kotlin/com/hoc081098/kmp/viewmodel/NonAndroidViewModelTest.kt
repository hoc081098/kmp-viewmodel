import com.hoc081098.kmp.viewmodel.DemoViewModel
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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
}
