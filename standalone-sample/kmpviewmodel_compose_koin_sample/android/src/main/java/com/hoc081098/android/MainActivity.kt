package com.hoc081098.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.createSavedStateHandle
import com.hoc081098.common.App
import com.hoc081098.kmp.viewmodel.compose.kmpViewModel
import com.hoc081098.kmp.viewmodel.koin.compose.koinKmpViewModel
import com.hoc081098.kmp.viewmodel.viewModelFactory

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      App()
      TestAndroidCompatibility()
    }
  }
}

@Composable
private fun TestAndroidCompatibility() {
  val viewModel1 = kmpViewModel(
    factory = viewModelFactory {
      MainActivityViewModel(
        savedStateHandle = createSavedStateHandle(),
      )
    },
  )

  val viewModel2 = koinKmpViewModel<MainActivityViewModel>()

  DisposableEffect(viewModel1, viewModel2) {
    check(viewModel1 === viewModel2)

    println("DisposableEffect viewModel=$viewModel1")
    onDispose { println("DisposableEffect disposed viewModel=$viewModel1") }
  }
}
