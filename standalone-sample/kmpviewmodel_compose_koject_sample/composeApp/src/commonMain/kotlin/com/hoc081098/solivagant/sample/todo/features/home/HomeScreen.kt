package com.hoc081098.solivagant.sample.todo.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hoc081098.kmp.viewmodel.koject.compose.kojectKmpViewModel
import com.hoc081098.solivagant.lifecycle.compose.collectAsStateWithLifecycle
import com.hoc081098.solivagant.sample.todo.features.home.HomeUiState.TodoItemUi
import com.hoc081098.solivagant.sample.todo.features.utils.MARGIN_SCROLLBAR
import com.hoc081098.solivagant.sample.todo.features.utils.VerticalScrollbar
import com.hoc081098.solivagant.sample.todo.features.utils.rememberScrollbarAdapter
import kmpviewmodel_koject_sample.composeapp.generated.resources.Res
import kmpviewmodel_koject_sample.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
internal fun HomeScreen(
  modifier: Modifier = Modifier,
  viewModel: HomeViewModel = kojectKmpViewModel(),
) {
  val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

  Scaffold(
    modifier = modifier,
    topBar = {
      CenterAlignedTopAppBar(
        title = { Text("Todo list") },
      )
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = viewModel::navigateToAdd,
      ) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = null,
        )
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
        is HomeUiState.Content -> {
          val listState = rememberLazyListState()

          Image(
            modifier = Modifier.matchParentSize(),
            painter = painterResource(Res.drawable.compose_multiplatform),
            contentDescription = null,
          )

          LazyColumn(
            modifier = Modifier.matchParentSize()
              .background(
                color = MaterialTheme.colorScheme
                  .background
                  .copy(alpha = 0.3f),
              ),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            items(
              items = s.items,
              key = { it.id },
              contentType = { "TodoItem" },
            ) { item ->
              Item(
                modifier = Modifier.fillMaxWidth(),
                item = item,
                onClicked = viewModel::navigateToDetail,
                onToggle = viewModel::toggle,
                onRemove = viewModel::remove,
              )
            }
          }

          VerticalScrollbar(
            modifier = Modifier
              .align(Alignment.CenterEnd)
              .fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState = listState),
          )
        }

        is HomeUiState.Error -> {
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

        HomeUiState.Loading -> {
          CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
          )
        }
      }
    }
  }
}

@Composable
private fun Item(
  item: TodoItemUi,
  onClicked: (TodoItemUi) -> Unit,
  onToggle: (TodoItemUi) -> Unit,
  onRemove: (TodoItemUi) -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier.clickable(onClick = { onClicked(item) }),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Spacer(modifier = Modifier.width(8.dp))

    Checkbox(
      checked = item.isDone,
      onCheckedChange = { onToggle(item) },
    )

    Spacer(modifier = Modifier.width(8.dp))

    Text(
      text = AnnotatedString(item.text),
      modifier = Modifier.weight(1f),
      maxLines = 2,
      overflow = TextOverflow.Ellipsis,
      style = MaterialTheme.typography.bodyLarge,
      textDecoration = if (item.isDone) TextDecoration.LineThrough else null,
    )

    Spacer(modifier = Modifier.width(8.dp))

    IconButton(onClick = { onRemove(item) }) {
      Icon(
        imageVector = Icons.Default.Delete,
        contentDescription = null,
      )
    }

    Spacer(modifier = Modifier.width(MARGIN_SCROLLBAR))
  }
}
