package com.hoc081098.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.createSavedStateHandle
import com.hoc081098.common.App
import com.hoc081098.kmp.viewmodel.compose.kmpViewModel
import com.hoc081098.kmp.viewmodel.viewModelFactory

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      MaterialTheme {
        val mainActivityViewModel = kmpViewModel(
          factory = viewModelFactory {
            MainActivityViewModel(
              savedStateHandle = createSavedStateHandle(),
            )
          },
        )

        DisposableEffect(mainActivityViewModel) {
          println("DisposableEffect mainActivityViewModel=$mainActivityViewModel")
          onDispose { println("DisposableEffect disposed mainActivityViewModel=$mainActivityViewModel") }
        }

        App()
      }
    }
  }
}
