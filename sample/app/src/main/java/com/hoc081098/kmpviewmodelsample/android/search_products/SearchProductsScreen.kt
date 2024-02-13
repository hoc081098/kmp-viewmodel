@file:OptIn(
  ExperimentalMaterial3Api::class,
  ExperimentalMaterialApi::class,
)

package com.hoc081098.kmpviewmodelsample.android.search_products

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hoc081098.kmpviewmodelsample.ProductItemUi
import com.hoc081098.kmpviewmodelsample.android.common.EmptyProducts
import com.hoc081098.kmpviewmodelsample.android.common.ErrorMessageAndRetryButton
import com.hoc081098.kmpviewmodelsample.android.common.LoadingIndicator
import com.hoc081098.kmpviewmodelsample.android.common.MyApplicationTheme
import com.hoc081098.kmpviewmodelsample.android.common.OnLifecycleEventWithBuilder
import com.hoc081098.kmpviewmodelsample.android.common.ProductItemsList
import com.hoc081098.kmpviewmodelsample.common.AppDispatchers
import com.hoc081098.kmpviewmodelsample.search_products.SearchProductsState
import com.hoc081098.kmpviewmodelsample.search_products.SearchProductsViewModel
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun SearchProductsScreen(
  navigateToProductDetail: (Int) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: SearchProductsViewModel = koinViewModel(),
) {
  OnLifecycleEventWithBuilder {
    onEach { owner, event -> Napier.d("[SearchProductsScreen] event=$event, owner=$owner") }
  }

  val state by viewModel.stateFlow.collectAsStateWithLifecycle()
  val searchTerm by viewModel.searchTermStateFlow.collectAsStateWithLifecycle(
    context = koinInject<AppDispatchers>().immediateMain,
  )

  Column(
    modifier = modifier.fillMaxSize(),
  ) {
    TextField(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      value = searchTerm.orEmpty(),
      onValueChange = viewModel::search,
      label = { Text(text = "Search term") },
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      text = "Submitted term: ${state.submittedTerm.orEmpty()}",
    )

    Spacer(modifier = Modifier.height(16.dp))

    Box(
      modifier = Modifier.weight(1f),
    ) {
      ListContent(
        modifier = Modifier.fillMaxSize(),
        state = state,
        onItemClick = { navigateToProductDetail(it.id) },
      )
    }
  }
}

@Composable
private fun ListContent(
  state: SearchProductsState,
  onItemClick: (ProductItemUi) -> Unit,
  modifier: Modifier = Modifier,
) {
  when {
    state.isLoading -> {
      LoadingIndicator(modifier = modifier)
    }

    state.error != null -> {
      ErrorMessageAndRetryButton(
        modifier = modifier,
        onRetry = { },
        errorMessage = state.error?.message ?: "Unknown error",
      )
    }

    state.products.isEmpty() -> {
      EmptyProducts(modifier = modifier)
    }

    else -> {
      ProductItemsList(
        modifier = modifier.fillMaxSize(),
        products = state.products,
        isRefreshing = false,
        pullRefreshState = null,
        onItemClick = onItemClick,
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun ListContentPreview(
  @PreviewParameter(ListContentParameterProvider::class) state: SearchProductsState,
) {
  MyApplicationTheme {
    ListContent(
      state = state,
      onItemClick = {},
    )
  }
}

@Suppress("MagicNumber")
private class ListContentParameterProvider : CollectionPreviewParameterProvider<SearchProductsState>(
  listOf(
    SearchProductsState(
      isLoading = true,
      error = null,
      products = persistentListOf(),
      submittedTerm = null,
    ),
    SearchProductsState(
      isLoading = false,
      error = RuntimeException("Network error"),
      products = persistentListOf(),
      submittedTerm = null,
    ),
    SearchProductsState(
      isLoading = false,
      error = null,
      products = persistentListOf(),
      submittedTerm = null,
    ),
    SearchProductsState(
      isLoading = false,
      error = null,
      products = List(10) {
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
      }.toImmutableList(),
      submittedTerm = null,
    ),
  ),
)
