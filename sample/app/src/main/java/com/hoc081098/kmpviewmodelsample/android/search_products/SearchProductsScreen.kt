@file:OptIn(
  ExperimentalMaterial3Api::class,
  ExperimentalMaterialApi::class,
)
@file:Suppress("PackageNaming")

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hoc081098.kmpviewmodelsample.ProductItemUi
import com.hoc081098.kmpviewmodelsample.android.common.EmptyProducts
import com.hoc081098.kmpviewmodelsample.android.common.ErrorMessageAndRetryButton
import com.hoc081098.kmpviewmodelsample.android.common.LoadingIndicator
import com.hoc081098.kmpviewmodelsample.android.common.OnLifecycleEventWithBuilder
import com.hoc081098.kmpviewmodelsample.android.common.ProductItemsList
import com.hoc081098.kmpviewmodelsample.common.AppDispatchers
import com.hoc081098.kmpviewmodelsample.search_products.SearchProductsState
import com.hoc081098.kmpviewmodelsample.search_products.SearchProductsViewModel
import io.github.aakira.napier.Napier
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.rememberKoinInject

@Suppress("ReturnCount")
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
    context = rememberKoinInject<AppDispatchers>().immediateMain,
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

@Suppress("ReturnCount", "ModifierReused")
@Composable
private fun ListContent(
  state: SearchProductsState,
  onItemClick: (ProductItemUi) -> Unit,
  modifier: Modifier = Modifier,
) {
  if (state.isLoading) {
    LoadingIndicator(
      modifier = modifier,
    )
    return
  }

  state.error?.let { error ->
    ErrorMessageAndRetryButton(
      modifier = modifier,
      onRetry = { },
      errorMessage = error.message ?: "Unknown error",
    )
    return
  }

  val products = state.products.ifEmpty {
    EmptyProducts(
      modifier = modifier,
    )
    return
  }

  ProductItemsList(
    modifier = Modifier.fillMaxSize(),
    products = products,
    isRefreshing = false,
    pullRefreshState = null,
    onItemClick = onItemClick,
  )
}
