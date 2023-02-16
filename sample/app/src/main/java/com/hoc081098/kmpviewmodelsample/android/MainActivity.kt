@file:OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)

package com.hoc081098.kmpviewmodelsample.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.hoc081098.kmpviewmodelsample.ProductItem
import com.hoc081098.kmpviewmodelsample.ProductSingleEvent
import com.hoc081098.kmpviewmodelsample.ProductsAction
import com.hoc081098.kmpviewmodelsample.ProductsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      MyApplicationTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background,
        ) {
          Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
              TopAppBar(
                title = {
                  Text(
                    text = "KMP ViewModel Sample",
                  )
                },
                navigationIcon = {
                  IconButton(onClick = { finish() }) {
                    Icon(
                      imageVector = Icons.Default.ArrowBack,
                      contentDescription = null,
                    )
                  }
                },
              )
            },
          ) { innerPadding ->
            ProductsContent(
              modifier = Modifier
                .padding(innerPadding)
                .consumedWindowInsets(innerPadding),
            )
          }
        }
      }
    }
  }
}

@Suppress("ReturnCount")
@Composable
fun ProductsContent(
  modifier: Modifier = Modifier,
  viewModel: ProductsViewModel = koinViewModel(),
) {
  LaunchedEffect(viewModel) { viewModel.dispatch(ProductsAction.Load) }

  val state by viewModel.stateFlow.collectAsStateWithLifecycle()

  val scope = rememberCoroutineScope()
  val lazyListState = rememberLazyListState()
  val currentLazyListState by rememberUpdatedState(lazyListState)

  viewModel.eventFlow.collectInLaunchedEffectWithLifecycle { event ->
    when (event) {
      is ProductSingleEvent.Refresh.Failure -> {}
      ProductSingleEvent.Refresh.Success -> {
        scope.launch {
          withFrameMillis { }
          currentLazyListState.animateScrollToItem(index = 0)
        }
      }
    }
  }

  if (state.isLoading) {
    LoadingIndicator(modifier = modifier)
    return
  }

  state.error?.let { error ->
    ErrorMessageAndRetryButton(
      modifier = modifier,
      onRetry = { viewModel.dispatch(ProductsAction.Load) },
      errorMessage = error.message ?: "Unknown error",
    )
    return
  }

  val products = state.products
  products.ifEmpty {
    EmptyProducts(modifier = modifier)
    return
  }
  ProductItemsList(
    modifier = modifier,
    products = products,
    isRefreshing = state.isRefreshing,
    onRefresh = { viewModel.dispatch(ProductsAction.Refresh) },
    lazyListState = lazyListState,
  )
}

@Composable
private fun EmptyProducts(modifier: Modifier) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    Text(text = "No products")
  }
}

@Composable
private fun LoadingIndicator(modifier: Modifier) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    CircularProgressIndicator()
  }
}

@Composable
private fun ErrorMessageAndRetryButton(
  errorMessage: String,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(text = errorMessage)

      Spacer(modifier = Modifier.height(16.dp))

      Button(onClick = onRetry) {
        Text(text = "Retry")
      }
    }
  }
}

@Composable
private fun ProductItemsList(
  products: List<ProductItem>,
  isRefreshing: Boolean,
  onRefresh: () -> Unit,
  modifier: Modifier = Modifier,
  lazyListState: LazyListState = rememberLazyListState(),
) {
  val pullRefreshState = rememberPullRefreshState(
    refreshing = isRefreshing,
    onRefresh = onRefresh,
  )

  Box(
    modifier = modifier,
  ) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .pullRefresh(state = pullRefreshState),
      state = lazyListState,
      verticalArrangement = Arrangement.spacedBy(16.dp),
      contentPadding = PaddingValues(16.dp),
    ) {
      items(
        items = products,
        key = { it.id },
      ) {
        ProductItemRow(
          modifier = Modifier.fillParentMaxWidth(),
          product = it,
        )
      }
    }

    PullRefreshIndicator(
      modifier = Modifier.align(Alignment.TopCenter),
      refreshing = isRefreshing,
      state = pullRefreshState,
      backgroundColor = MaterialTheme.colorScheme.surface,
      contentColor = contentColorFor(backgroundColor = MaterialTheme.colorScheme.surface),
    )
  }
}

@Composable
fun ProductItemRow(
  product: ProductItem,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier,
  ) {
    SubcomposeAsyncImage(
      model = product.images.firstOrNull(),
      contentDescription = product.title,
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .size(96.dp)
        .clip(RoundedCornerShape(4.dp)),
    ) {
      when (painter.state) {
        is AsyncImagePainter.State.Loading -> {
          CircularProgressIndicator(
            modifier = Modifier
              .wrapContentSize(Alignment.Center),
            strokeWidth = 2.dp,
          )
        }
        is AsyncImagePainter.State.Error -> {
          Icon(
            modifier = Modifier
              .wrapContentSize(Alignment.Center),
            imageVector = Icons.Default.Warning,
            contentDescription = "Error",
          )
        }
        else -> {
          SubcomposeAsyncImageContent()
        }
      }
    }

    Spacer(modifier = Modifier.width(16.dp))

    Column(
      modifier = Modifier
        .weight(1f),
      verticalArrangement = Arrangement.Top,
    ) {
      Text(
        text = product.title,
        style = MaterialTheme.typography.headlineSmall,
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = product.description,
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
      )
    }
  }
}
