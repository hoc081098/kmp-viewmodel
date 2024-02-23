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
import org.koin.androidx.compose.koinViewModel

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
  // kmpViewModel with viewModelFactory
  val viewModel1 = kmpViewModel(
    factory = viewModelFactory {
      MainActivityViewModel(
        savedStateHandle = createSavedStateHandle(),
      )
    },
  )

  // kmpViewModel with lambda
  val viewModel2 = kmpViewModel {
    MainActivityViewModel(
      savedStateHandle = createSavedStateHandle(),
    )
  }

  // koinKmpViewModel
  val viewModel3 = koinKmpViewModel<MainActivityViewModel>()

  // koinViewModel
  val viewModel4 = koinViewModel<MainActivityViewModel>()

  DisposableEffect(viewModel1, viewModel2, viewModel3, viewModel4) {
    check(
      setOf(
        viewModel1,
        viewModel2,
        viewModel3,
        viewModel4,
      ).size == 1
    ) {
      "viewModel1=$viewModel1, viewModel2=$viewModel2, viewModel3=$viewModel3, viewModel4=$viewModel4" +
          "They must be the same instance"
    }

    println("DisposableEffect viewModel=$viewModel1")
    onDispose { println("DisposableEffect disposed viewModel=$viewModel1") }
  }
}
