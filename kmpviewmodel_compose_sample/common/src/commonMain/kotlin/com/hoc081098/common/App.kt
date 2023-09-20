package com.hoc081098.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hoc081098.common.navigation.LocalNavigator
import com.hoc081098.common.navigation.NavHost
import com.hoc081098.common.navigation.Route
import com.hoc081098.common.navigation.routeContent
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.compose.kmpViewModel
import com.hoc081098.kmp.viewmodel.createSavedStateHandle
import com.hoc081098.kmp.viewmodel.parcelable.Parcelize

@Parcelize
data object ScreenA : Route

@Parcelize
data class ScreenB(val id: Int) : Route

@Parcelize
data class ScreenC(val id: Int) : Route

class ScreenAViewModel(
  private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
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

class ScreenBViewModel(
  private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
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

class ScreenCViewModel(
  private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
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
      Column {
        Text(
          text = "ViewModel: $viewModel",
          style = MaterialTheme.typography.h6,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "Saved count: $savedCount",
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

val ScreenBContent = routeContent<ScreenB> { route ->
  val navigator = LocalNavigator.current
  var count by rememberSaveable { mutableStateOf(0) }

  val viewModel = kmpViewModel(
    factory = {
      ScreenBViewModel(
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
        },
        navigationIcon = {
          IconButton(
            onClick = navigator::navigateBack
          ) {
            Icon(
              imageVector = Icons.Default.ArrowBack,
              contentDescription = null,
            )
          }
        }
      )
    },
    backgroundColor = Color.Red.copy(alpha = 0.5f),
  ) {
    Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center
    ) {
      Column {
        Text(
          text = "ViewModel: $viewModel",
          style = MaterialTheme.typography.h6,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "Saved count: $savedCount",
          style = MaterialTheme.typography.h6,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Spacer(modifier = Modifier.height(8.dp))

        Button(
          onClick = {
            viewModel.inc()
            count++
            navigator.navigateTo(ScreenC(id = count))
          }
        ) {
          Text(text = "To ScreenC")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Click count: $count")
      }
    }
  }
}

val ScreenCContent = routeContent<ScreenC> { route ->
  val navigator = LocalNavigator.current

  val viewModel = kmpViewModel(
    factory = {
      ScreenCViewModel(
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
        },
        navigationIcon = {
          IconButton(
            onClick = navigator::navigateBack
          ) {
            Icon(
              imageVector = Icons.Default.ArrowBack,
              contentDescription = null,
            )
          }
        }
      )
    },
    backgroundColor = Color.Cyan.copy(alpha = 0.5f),
  ) {
    Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center
    ) {
      Column {
        Text(
          text = "ViewModel: $viewModel",
          style = MaterialTheme.typography.h6,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "Saved count: $savedCount",
          style = MaterialTheme.typography.h6,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
          onClick = {
            viewModel.inc()
          }
        ) {
          Text(text = "Inc")
        }
      }
    }
  }
}

@Composable
fun App() {
  MaterialTheme {
    Surface(
      modifier = Modifier.fillMaxSize(),
      color = MaterialTheme.colors.background,
    ) {
      NavHost(
        initialRoute = ScreenA,
        contents = listOf(
          ScreenAContent,
          ScreenBContent,
          ScreenCContent,
        )
      )
    }
  }
}
