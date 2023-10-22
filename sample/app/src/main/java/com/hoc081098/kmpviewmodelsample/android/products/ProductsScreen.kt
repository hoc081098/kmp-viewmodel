@file:OptIn(ExperimentalMaterialApi::class)

package com.hoc081098.kmpviewmodelsample.android.products

import android.widget.Toast
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hoc081098.kmpviewmodelsample.android.common.CollectWithLifecycleEffect
import com.hoc081098.kmpviewmodelsample.android.common.EmptyProducts
import com.hoc081098.kmpviewmodelsample.android.common.ErrorMessageAndRetryButton
import com.hoc081098.kmpviewmodelsample.android.common.LoadingIndicator
import com.hoc081098.kmpviewmodelsample.android.common.OnLifecycleEventWithBuilder
import com.hoc081098.kmpviewmodelsample.android.common.ProductItemsList
import com.hoc081098.kmpviewmodelsample.products.ProductSingleEvent
import com.hoc081098.kmpviewmodelsample.products.ProductsAction
import com.hoc081098.kmpviewmodelsample.products.ProductsViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Suppress("ReturnCount", "ModifierReused")
@Composable
fun ProductsScreen(
  navigateToProductDetail: (Int) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: ProductsViewModel = koinViewModel(),
) {
  LaunchedEffect(viewModel) {
    if (!viewModel.stateFlow.value.hasContent) {
      viewModel.dispatch(ProductsAction.Load)
    }
  }

  OnLifecycleEventWithBuilder {
    onEach { owner, event -> Napier.d("[ProductsScreen] event=$event, owner=$owner") }
  }

  val state by viewModel.stateFlow.collectAsStateWithLifecycle()

  val scope = rememberCoroutineScope()
  val lazyListState = rememberLazyListState()
  val currentLazyListState by rememberUpdatedState(lazyListState)
  val context = LocalContext.current

  val eventHandler: (ProductSingleEvent) -> Unit = remember(context, scope, currentLazyListState) {
    {
        event ->
      when (event) {
        is ProductSingleEvent.Refresh.Failure -> {
          Toast.makeText(context, "Failed to refresh", Toast.LENGTH_SHORT).show()
        }

        ProductSingleEvent.Refresh.Success -> {
          scope.launch {
            withFrameMillis { }
            currentLazyListState.animateScrollToItem(index = 0)
          }
        }
      }
    }
  }
  viewModel.eventFlow.CollectWithLifecycleEffect(collector = eventHandler)

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

  val products = state.products.ifEmpty {
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
    onItemClick = { navigateToProductDetail(it.id) },
  )
}
