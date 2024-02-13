@file:OptIn(ExperimentalMaterialApi::class)

package com.hoc081098.kmpviewmodelsample.android.products

import android.widget.Toast
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hoc081098.kmpviewmodelsample.ProductItemUi
import com.hoc081098.kmpviewmodelsample.android.common.CollectWithLifecycleEffect
import com.hoc081098.kmpviewmodelsample.android.common.EmptyProducts
import com.hoc081098.kmpviewmodelsample.android.common.ErrorMessageAndRetryButton
import com.hoc081098.kmpviewmodelsample.android.common.LoadingIndicator
import com.hoc081098.kmpviewmodelsample.android.common.MyApplicationTheme
import com.hoc081098.kmpviewmodelsample.android.common.OnLifecycleEventWithBuilder
import com.hoc081098.kmpviewmodelsample.android.common.ProductItemsList
import com.hoc081098.kmpviewmodelsample.products.ProductSingleEvent
import com.hoc081098.kmpviewmodelsample.products.ProductsAction
import com.hoc081098.kmpviewmodelsample.products.ProductsState
import com.hoc081098.kmpviewmodelsample.products.ProductsViewModel
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductsScreen(
  navigateToProductDetail: (Int) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: ProductsViewModel = koinViewModel(),
) {
  val dispatch: (ProductsAction) -> Unit = remember(viewModel) { viewModel::dispatch }

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

  ListContent(
    modifier = modifier,
    state = state,
    lazyListState = lazyListState,
    navigateToProductDetail = navigateToProductDetail,
    onRetry = { dispatch(ProductsAction.Load) },
    onRefresh = { dispatch(ProductsAction.Refresh) },
  )
}

@Composable
private fun ListContent(
  state: ProductsState,
  navigateToProductDetail: (Int) -> Unit,
  onRetry: () -> Unit,
  onRefresh: () -> Unit,
  modifier: Modifier = Modifier,
  lazyListState: LazyListState = rememberLazyListState(),
) {
  Surface(modifier = modifier) {
    when {
      state.isLoading -> {
        LoadingIndicator()
      }

      state.error != null -> {
        ErrorMessageAndRetryButton(
          onRetry = onRetry,
          errorMessage = state.error?.message ?: "Unknown error",
        )
      }

      state.products.isEmpty() -> {
        EmptyProducts()
      }

      else -> {
        ProductItemsList(
          products = state.products,
          pullRefreshState = rememberPullRefreshState(
            refreshing = state.isRefreshing,
            onRefresh = onRefresh,
          ),
          isRefreshing = state.isRefreshing,
          lazyListState = lazyListState,
          onItemClick = { navigateToProductDetail(it.id) },
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun ListContentPreview(
  @PreviewParameter(ListContentParameterProvider::class) state: ProductsState,
) {
  MyApplicationTheme {
    ListContent(
      state = state,
      navigateToProductDetail = {},
      onRetry = {},
      onRefresh = {},
    )
  }
}

@Suppress("MagicNumber")
private class ListContentParameterProvider : CollectionPreviewParameterProvider<ProductsState>(
  run {
    val products = List(10) {
      ProductItemUi(
        id = it,
        title = "title $it",
        price = it,
        description = "description $it",
        images = persistentListOf(),
        creationAt = "2023-02-11T02:45:59.000Z",
        updatedAt = "2023-02-11T14:54:14.000Z",
        category = ProductItemUi.CategoryUi(
          id = it,
          name = "category name $it",
          image = "image",
          creationAt = "2023-02-11T02:45:59.000Z",
          updatedAt = "2023-02-11T14:54:14.000Z",
        ),
      )
    }.toImmutableList()

    listOf(
      ProductsState(
        isLoading = true,
        error = null,
        products = persistentListOf(),
        isRefreshing = false,
      ),
      ProductsState(
        isLoading = false,
        error = RuntimeException("Network error"),
        products = persistentListOf(),
        isRefreshing = false,
      ),
      ProductsState(
        isLoading = false,
        error = null,
        products = persistentListOf(),
        isRefreshing = false,
      ),
      ProductsState(
        isLoading = false,
        error = null,
        products = products,
        isRefreshing = false,
      ),
      ProductsState(
        isLoading = false,
        error = null,
        products = products,
        isRefreshing = true,
      ),
    )
  },
)
