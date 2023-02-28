@file:OptIn(
  ExperimentalMaterial3Api::class,
  ExperimentalMaterialApi::class,
)
@file:Suppress("PackageNaming")

package com.hoc081098.kmpviewmodelsample.android.search_products

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hoc081098.kmpviewmodelsample.android.common.EmptyProducts
import com.hoc081098.kmpviewmodelsample.android.common.ErrorMessageAndRetryButton
import com.hoc081098.kmpviewmodelsample.android.common.LoadingIndicator
import com.hoc081098.kmpviewmodelsample.android.common.MyApplicationTheme
import com.hoc081098.kmpviewmodelsample.android.common.ProductItemsList
import com.hoc081098.kmpviewmodelsample.search_products.SearchProductsState
import com.hoc081098.kmpviewmodelsample.search_products.SearchProductsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalLayoutApi::class)
class SearchProductsActivity : ComponentActivity() {
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
                    text = "Search products screen",
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
            SearchProductsContent(
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
private fun SearchProductsContent(
  modifier: Modifier = Modifier,
  viewModel: SearchProductsViewModel = koinViewModel(),
) {
  val state by viewModel.stateFlow.collectAsStateWithLifecycle()
  val searchTerm by viewModel.searchTermStateFlow.collectAsStateWithLifecycle()

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
      )
    }
  }
}

@Suppress("ReturnCount")
@Composable
private fun ListContent(
  modifier: Modifier = Modifier,
  state: SearchProductsState,
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
  )
}
