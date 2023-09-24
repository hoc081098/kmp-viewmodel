package com.hoc081098.common.navigation

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.hoc081098.common.navigation.internal.DefaultNavigator

@Composable
internal actual fun SystemBackHandlingEffect(navigator: DefaultNavigator) {
  val backPressedDispatcherOwner = requireNotNull(LocalOnBackPressedDispatcherOwner.current) {
    "No OnBackPressedDispatcher available"
  }
  val lifecycleOwner = LocalLifecycleOwner.current

  val callback = remember(navigator) {
    object : OnBackPressedCallback(enabled = navigator.canNavigateBackState.value) {
      override fun handleOnBackPressed() = navigator.navigateBack()
    }
  }

  LaunchedEffect(callback, navigator) {
    snapshotFlow { navigator.canNavigateBackState.value }
      .collect { callback.isEnabled = it }
  }

  DisposableEffect(backPressedDispatcherOwner, callback, lifecycleOwner) {
    backPressedDispatcherOwner.onBackPressedDispatcher.addCallback(
      lifecycleOwner,
      callback,
    )
    onDispose(callback::remove)
  }
}
