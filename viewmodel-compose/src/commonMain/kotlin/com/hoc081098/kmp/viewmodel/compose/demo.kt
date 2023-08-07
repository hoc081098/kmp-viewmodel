package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.hoc081098.kmp.viewmodel.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

public class DemoViewModel : ViewModel() {
  internal val stateFlow: StateFlow<Int> = (1..100).asFlow()
    .onEach { delay(1000) }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.Lazily,
      initialValue = 0,
    )
}

@Composable
public fun DemoScreen(
  viewModel1: DemoViewModel = kmpViewModel(
    key = "DemoViewModel1",
    factory = { DemoViewModel() },
  ),
  viewModel2: DemoViewModel = kmpViewModel(
    key = "DemoViewModel2",
    factory = rememberViewModelFactory { DemoViewModel() },
  ),
  viewModel3: DemoViewModel = kmpViewModel(
    key = "DemoViewModel3",
    factory = rememberViewModelFactory("key") { DemoViewModel() },
  ),
) {
  viewModel1.stateFlow.collectAsState().value
  viewModel2.stateFlow.collectAsState().value

  LaunchedEffect(viewModel1) {
    viewModel1.addCloseable {
      println("DemoViewModel1 is cleared")
    }
  }
  LaunchedEffect(viewModel2) {
    viewModel2.addCloseable {
      println("DemoViewModel1 is cleared")
    }
  }
}
