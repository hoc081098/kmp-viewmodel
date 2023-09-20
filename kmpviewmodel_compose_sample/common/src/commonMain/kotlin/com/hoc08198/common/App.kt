package com.hoc08198.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hoc081098.kmp.viewmodel.parcelable.Parcelize
import com.hoc08198.common.navigation.LocalNavigator
import com.hoc08198.common.navigation.NavHost
import com.hoc08198.common.navigation.Route
import com.hoc08198.common.navigation.routeContent

@Parcelize
data object ScreenA : Route

@Parcelize
data class ScreenB(val id: Int) : Route

@Parcelize
data class ScreenC(val id: Int) : Route

val ScreenAContent = routeContent<ScreenA> {
  val navigator = LocalNavigator.current

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(text = "Screen A")
        }
      )
    }
  ) {
    Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center,
    ) {
      Button(
        onClick = {
          navigator.navigateTo(ScreenB(1))
        }
      ) {
        Text(text = "To B")
      }
    }
  }
}

val ScreenBContent = routeContent<ScreenB> { screenB ->
  val navigator = LocalNavigator.current
  var count by rememberSaveable { mutableStateOf(0) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(text = "Screen B(${screenB.id})")
        }
      )
    }
  ) {
    Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center
    ) {
      Column {
        Button(
          onClick = {
            count++
            navigator.navigateTo(ScreenC(id = screenB.id + 1))
          }
        ) {
          Text(text = "To C count=$count")
        }

        Button(
          onClick = navigator::navigateBack
        ) {
          Text(text = "Pop")
        }
      }
    }
  }
}

val ScreenCContent = routeContent<ScreenC> { screenC ->
  val navigator = LocalNavigator.current

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(text = "Screen C(${screenC.id})")
        }
      )
    }
  ) {
    Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center
    ) {
      Button(onClick = navigator::navigateBack) {
        Text(text = "Pop")
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
