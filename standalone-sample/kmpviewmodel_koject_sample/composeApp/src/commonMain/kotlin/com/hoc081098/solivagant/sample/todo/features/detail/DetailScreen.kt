package com.hoc081098.solivagant.sample.todo.features.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hoc081098.kmp.viewmodel.compose.kmpViewModel
import com.hoc081098.kmp.viewmodel.koject.kojectViewModelFactory
import com.hoc081098.solivagant.lifecycle.compose.collectAsStateWithLifecycle
import com.hoc081098.solivagant.sample.todo.features.detail.DetailUiState.TodoItemUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailScreen(
  route: DetailScreenRoute,
  modifier: Modifier = Modifier,
  viewModel: DetailViewModel = kmpViewModel(kojectViewModelFactory()),
) {
  val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

  Scaffold(
    modifier = modifier,
    topBar = {
      CenterAlignedTopAppBar(
        title = { Text(text = "Detail #${route.id}") },
        navigationIcon = {
          IconButton(onClick = viewModel::navigateBack) {
            Icon(
              imageVector = Icons.AutoMirrored.Default.ArrowBack,
              contentDescription = null,
            )
          }
        },
      )
    },
    floatingActionButton = {
      uiState
        .takeIf { it is DetailUiState.Content && it.item != null }
        ?.let {
          FloatingActionButton(onClick = viewModel::navigateToEdit) {
            Icon(
              imageVector = Icons.Default.Edit,
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
        is DetailUiState.Content -> {
          ItemContent(
            modifier = Modifier.fillMaxSize(),
            item = s.item,
            onToggle = viewModel::toggle,
          )
        }

        is DetailUiState.Error -> {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
          ) {
            Text(
              text = s.message,
              modifier = Modifier.fillMaxWidth(),
              textAlign = TextAlign.Center,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(12.dp))

            ElevatedButton(onClick = {}) {
              Text("Click to retry")
            }
          }
        }

        DetailUiState.Loading -> {
          CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
          )
        }
      }
    }
  }
}

@Composable
private fun ItemContent(
  item: TodoItemUi?,
  onToggle: (TodoItemUi) -> Unit,
  modifier: Modifier = Modifier,
) {
  Card(
    modifier = modifier,
    elevation = CardDefaults.elevatedCardElevation(),
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .height(IntrinsicSize.Min),
    ) {
      ListItem(
        headlineContent = {
          Text(
            text = item?.text ?: "Not found",
            style = MaterialTheme.typography.titleMedium,
            textDecoration = if (item?.isDone == true) TextDecoration.LineThrough else null,
          )
        },
        trailingContent = {
          item?.let {
            Checkbox(
              checked = item.isDone,
              onCheckedChange = { onToggle(item) },
            )
          }
        },
      )
    }
  }
}
