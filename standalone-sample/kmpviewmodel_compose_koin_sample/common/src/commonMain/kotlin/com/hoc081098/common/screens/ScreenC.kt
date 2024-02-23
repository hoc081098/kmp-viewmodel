package com.hoc081098.common.screens

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
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hoc081098.common.domain.DemoRepository
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.koin.compose.koinKmpViewModel
import com.hoc081098.kmp.viewmodel.parcelable.Parcelize
import com.hoc081098.solivagant.lifecycle.compose.collectAsStateWithLifecycle
import com.hoc081098.solivagant.navigation.NavEventNavigator
import com.hoc081098.solivagant.navigation.NavRoute
import com.hoc081098.solivagant.navigation.ScreenDestination
import com.hoc081098.solivagant.navigation.requireRoute
import kotlin.jvm.JvmField
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Parcelize
data class ScreenC(val id: Int) : NavRoute

class ScreenCViewModel(
  private val savedStateHandle: SavedStateHandle,
  private val demoRepository: DemoRepository,
) : ViewModel() {
  val route = savedStateHandle.requireRoute<ScreenC>()

  val countStateFlow = savedStateHandle.getStateFlow("count", 0)

  init {
    println("$this init")

    addCloseable {
      println("$this close")
    }

    viewModelScope.launch {
      val data = demoRepository.getData()
      println("$this data=$data")
    }
  }

  fun inc() {
    savedStateHandle["count"] = countStateFlow.value + 1
  }
}

@JvmField
val ScreenCContent = ScreenDestination<ScreenC> { route, modifier ->
  val viewModel = koinKmpViewModel<ScreenCViewModel>()
  val navigator = koinInject<NavEventNavigator>()

  val savedCount by viewModel.countStateFlow.collectAsStateWithLifecycle()

  Scaffold(
    modifier = modifier,
    topBar = {
      TopAppBar(
        title = {
          Text(text = route.toString())
        },
        navigationIcon = {
          IconButton(
            onClick = navigator::navigateBack,
          ) {
            Icon(
              imageVector = Icons.Default.ArrowBack,
              contentDescription = null,
            )
          }
        },
      )
    },
    backgroundColor = Color.Cyan.copy(alpha = 0.3f),
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
          textAlign = TextAlign.Center,
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
          },
        ) {
          Text(text = "Inc")
        }
      }
    }
  }
}
