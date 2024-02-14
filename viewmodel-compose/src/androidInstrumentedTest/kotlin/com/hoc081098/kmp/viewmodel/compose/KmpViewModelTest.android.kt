package com.hoc081098.kmp.viewmodel.compose

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.IntState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.viewmodel.compose.saveable
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.createSavedStateHandle
import org.junit.Rule
import org.junit.Test

class TestViewModel(
  savedStateHandle: SavedStateHandle,
) : ViewModel() {
  private val countState = savedStateHandle.saveable("count") { mutableIntStateOf(0) }
  val count: IntState get() = countState
  fun increment() = countState.intValue++
}

@Composable
fun TestApp(
  viewModel: TestViewModel = kmpViewModel {
    TestViewModel(
      savedStateHandle = createSavedStateHandle(),
    )
  }
) {
  LaunchedEffect(viewModel) {
    viewModel.increment()
  }
}

class KmpViewModelTest {
  @get:Rule
  val composeTestRule = createAndroidComposeRule<ComponentActivity>()

  @Test
  fun aaaaa() {
    // Setup compact window
    val stateRestorationTester = StateRestorationTester(composeTestRule)
    stateRestorationTester.setContent { TestApp() }

    // Simulate a config change
    stateRestorationTester.emulateSavedInstanceStateRestore()
  }
}
