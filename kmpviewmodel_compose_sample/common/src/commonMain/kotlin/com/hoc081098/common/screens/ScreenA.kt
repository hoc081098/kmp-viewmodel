package com.hoc081098.common.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hoc081098.common.navigation.LocalNavigator
import com.hoc081098.common.navigation.Route
import com.hoc081098.common.navigation.requireRoute
import com.hoc081098.common.navigation.routeContent
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.compose.kmpViewModel
import com.hoc081098.kmp.viewmodel.createSavedStateHandle
import com.hoc081098.kmp.viewmodel.parcelable.Parcelize

@Parcelize
data object ScreenA : Route

class ScreenAViewModel(
  private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
  val route = savedStateHandle.requireRoute<ScreenA>()

  val countStateFlow = savedStateHandle.getStateFlow("count", 0)

  init {
    println("$this init")

    addCloseable {
      println("$this close")
    }
  }

  fun inc() {
    savedStateHandle["count"] = countStateFlow.value + 1
  }
}

@JvmField
val ScreenAContent = routeContent<ScreenA> { route ->
  val navigator = LocalNavigator.current

  val viewModel = kmpViewModel(
    factory = {
      ScreenAViewModel(
        savedStateHandle = createSavedStateHandle()
      )
    },
  )

  val savedCount by viewModel.countStateFlow.collectAsState()

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(text = route.toString())
        }
      )
    },
    backgroundColor = Color.Green.copy(alpha = 0.5f),
  ) {
    Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center,
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Text(
          text = "ViewModel: $viewModel",
          style = MaterialTheme.typography.h6,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "Saved count (SavedStateHandle): $savedCount",
          style = MaterialTheme.typography.h6,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
          onClick = {
            viewModel.inc()
            navigator.navigateTo(ScreenB(1))
          }
        ) {
          Text(text = "To ScreenB")
        }
      }
    }
  }
}
