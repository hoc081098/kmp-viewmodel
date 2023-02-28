@file:OptIn(
  ExperimentalLayoutApi::class,
  ExperimentalMaterial3Api::class,
  ExperimentalMaterialApi::class,
)

package com.hoc081098.kmpviewmodelsample.android.products

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hoc081098.kmpviewmodelsample.android.common.EmptyProducts
import com.hoc081098.kmpviewmodelsample.android.common.ErrorMessageAndRetryButton
import com.hoc081098.kmpviewmodelsample.android.common.LoadingIndicator
import com.hoc081098.kmpviewmodelsample.android.common.MyApplicationTheme
import com.hoc081098.kmpviewmodelsample.android.common.ProductItemsList
import com.hoc081098.kmpviewmodelsample.android.common.collectInLaunchedEffectWithLifecycle
import com.hoc081098.kmpviewmodelsample.products.ProductSingleEvent
import com.hoc081098.kmpviewmodelsample.products.ProductsAction
import com.hoc081098.kmpviewmodelsample.products.ProductsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class ProductsActivity : ComponentActivity() {
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
                    text = "Products screen",
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
private fun ProductsContent(
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
    pullRefreshState = rememberPullRefreshState(
      refreshing = state.isRefreshing,
      onRefresh = { viewModel.dispatch(ProductsAction.Refresh) },
    ),
    isRefreshing = state.isRefreshing,
    lazyListState = lazyListState,
  )
}
