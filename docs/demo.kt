package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.hoc081098.kmp.viewmodel.EmptyCreationExtras
import com.hoc081098.kmp.viewmodel.SAVED_STATE_HANDLE_KEY
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.VIEW_MODEL_KEY
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.buildCreationExtras
import com.hoc081098.kmp.viewmodel.createSavedStateHandle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

public val isAndroid: Boolean = false

public class DemoViewModel(
  private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
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
    factory = {
      val key = checkNotNull(this[VIEW_MODEL_KEY])
      val savedStateHandle = createSavedStateHandle()
      DemoViewModel(
        savedStateHandle = savedStateHandle,
      )
    },
  ),
  viewModel2: DemoViewModel = kmpViewModel(
    key = "DemoViewModel2",
    factory = rememberViewModelFactory {
      val key = checkNotNull(this[VIEW_MODEL_KEY])
      val savedStateHandle = createSavedStateHandle()
      DemoViewModel(
        savedStateHandle = savedStateHandle,
      )
    },
  ),
  viewModel3: DemoViewModel = kmpViewModel(
    factory = rememberViewModelFactory(key = "key") {
      val key = checkNotNull(this[VIEW_MODEL_KEY])
      val savedStateHandle = createSavedStateHandle()
      DemoViewModel(
        savedStateHandle = savedStateHandle,
      )
    },
    extras = if (isAndroid) {
      EmptyCreationExtras
    } else {
      buildCreationExtras {
        this[SAVED_STATE_HANDLE_KEY] = SavedStateHandle(
          mapOf("id" to "value"),
        )
      }
    },
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
