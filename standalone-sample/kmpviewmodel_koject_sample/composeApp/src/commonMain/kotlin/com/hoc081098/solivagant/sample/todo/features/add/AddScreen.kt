package com.hoc081098.solivagant.sample.todo.features.add

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hoc081098.kmp.viewmodel.compose.kmpViewModel
import com.hoc081098.kmp.viewmodel.koject.kojectViewModelFactory
import com.hoc081098.solivagant.lifecycle.compose.LifecycleResumeEffect
import com.hoc081098.solivagant.lifecycle.compose.collectAsStateWithLifecycle
import com.hoc081098.solivagant.sample.todo.features.utils.CollectWithLifecycleEffect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddScreen(
  modifier: Modifier = Modifier,
  viewModel: AddViewModel = kmpViewModel(kojectViewModelFactory()),
) {
  val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle(
    context = viewModel.viewModelScope.coroutineContext,
  )

  var showConfirmBackDialog by rememberSaveable { mutableStateOf(false) }
  viewModel.singleEventFlow.CollectWithLifecycleEffect { event ->
    when (event) {
      AddSingleEvent.ConfirmBack -> {
        showConfirmBackDialog = true
      }
    }
  }

  // Handle back-press on Android.
  LifecycleResumeEffect(viewModel) {
    val job = viewModel
      .backPressesFlow
      .onEach { viewModel.confirmBack() }
      .launchIn(viewModel.viewModelScope)
    onPauseOrDispose { job.cancel() }
  }

  Scaffold(
    modifier = modifier,
    topBar = {
      CenterAlignedTopAppBar(
        title = { Text(text = "Add new todo") },
        navigationIcon = {
          IconButton(onClick = viewModel::confirmBack) {
            Icon(
              imageVector = Icons.AutoMirrored.Default.ArrowBack,
              contentDescription = null,
            )
          }
        },
      )
    },
    floatingActionButton = {
      if (uiState is AddUiState.Content) {
        FloatingActionButton(onClick = viewModel::save) {
          Icon(
            imageVector = Icons.Default.Done,
            contentDescription = null,
          )
        }
      }
    },
  ) { innerPadding ->
    Box(
      modifier = Modifier.fillMaxSize()
        .padding(innerPadding)
        .consumeWindowInsets(innerPadding),
      contentAlignment = Alignment.Center,
    ) {
      when (val s = uiState) {
        is AddUiState.Content -> {
          AddContent(
            modifier = Modifier.fillMaxSize(),
            content = s,
            onTextChange = viewModel::onTextChange,
            onDoneChange = viewModel::onDoneChange,
          )
        }

        AddUiState.Complete -> Unit
      }
    }
  }

  if (showConfirmBackDialog) {
    AlertDialog(
      onDismissRequest = { showConfirmBackDialog = false },
      title = { Text("Save changes?") },
      confirmButton = {
        TextButton(
          onClick = {
            showConfirmBackDialog = false
            viewModel.save()
          },
        ) {
          Text("Save")
        }
      },
      dismissButton = {
        TextButton(
          onClick = {
            showConfirmBackDialog = false
            viewModel.navigateBack()
          },
        ) {
          Text("Back without saving")
        }
      },
    )
  }
}

@Composable
private fun AddContent(
  content: AddUiState.Content,
  onTextChange: (String) -> Unit,
  onDoneChange: (Boolean) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .padding(8.dp)
      .height(IntrinsicSize.Min),
  ) {
    OutlinedTextField(
      modifier = Modifier.fillMaxWidth(),
      value = content.text,
      onValueChange = onTextChange,
      label = { Text("Text") },
      maxLines = 1,
      singleLine = true,
      leadingIcon = {
        Checkbox(
          checked = content.isDone,
          onCheckedChange = onDoneChange,
        )
      },
    )
  }
}
