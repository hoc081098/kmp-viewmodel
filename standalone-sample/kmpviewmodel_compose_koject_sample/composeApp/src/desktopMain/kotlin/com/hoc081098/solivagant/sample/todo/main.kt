package com.hoc081098.solivagant.sample.todo

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.saveable.LocalSaveableStateRegistry
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.hoc081098.kmp.viewmodel.compose.LocalSavedStateHandleFactory
import com.hoc081098.kmp.viewmodel.compose.LocalViewModelStoreOwner
import com.hoc081098.solivagant.lifecycle.LifecycleRegistry
import com.hoc081098.solivagant.lifecycle.LocalLifecycleOwner
import com.hoc081098.solivagant.lifecycle.rememberLifecycleOwner
import com.hoc081098.solivagant.navigation.LifecycleControllerEffect
import com.hoc081098.solivagant.navigation.SavedStateSupport

fun main() {
  startKoject {}

  val lifecycleRegistry = LifecycleRegistry()
  val savedStateSupport = SavedStateSupport()

  application {
    val windowState = rememberWindowState()
    LifecycleControllerEffect(lifecycleRegistry, windowState)

    // Clear the SavedStateSupport when the root @Composable exits the composition.
    DisposableEffect(Unit) {
      onDispose(savedStateSupport::clear)
    }

    Window(
      onCloseRequest = ::exitApplication,
      title = "Compose-Multiplatform-Todo-solivagant-Sample",
      state = windowState,
    ) {
      CompositionLocalProvider(
        LocalLifecycleOwner provides rememberLifecycleOwner(lifecycleRegistry),
        LocalViewModelStoreOwner provides savedStateSupport,
        LocalSaveableStateRegistry provides savedStateSupport,
        LocalSavedStateHandleFactory provides savedStateSupport,
      ) {
        TodoApp()

        // Must be at the last,
        // because onDispose is called in reverse order, so we want to save state first,
        // before [SaveableStateRegistry.Entry]s are unregistered.
        DisposableEffect(Unit) {
          onDispose(savedStateSupport::performSave)
        }
      }
    }
  }
}

@Preview
@Composable
fun AppDesktopPreview() {
  TodoApp()
}
