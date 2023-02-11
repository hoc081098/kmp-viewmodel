@file:OptIn(ExperimentalLayoutApi::class)

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.hoc081098.kmpviewmodelsample.ProductItem
import com.hoc081098.kmpviewmodelsample.ProductsAction
import com.hoc081098.kmpviewmodelsample.ProductsViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MyApplicationTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colors.background,
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

  val state by viewModel.stateFlow.collectAsState()

  if (state.isLoading) {
    Box(
      modifier = modifier.fillMaxSize(),
      contentAlignment = Alignment.Center,
    ) {
      CircularProgressIndicator()
    }
    return
  }

  state.error?.let { error ->
    Box(
      modifier = modifier.fillMaxSize(),
      contentAlignment = Alignment.Center,
    ) {
      Text(text = error.message ?: "Unknown error")
    }
    return
  }

  val products = state.products
  if (products.isEmpty()) {
    Box(
      modifier = modifier.fillMaxSize(),
      contentAlignment = Alignment.Center,
    ) {
      Text(text = "No products")
    }
    return
  }

  ProductItemsList(
    products = products,
    modifier = modifier,
  )
}

@Composable
private fun ProductItemsList(
  products: List<ProductItem>,
  modifier: Modifier = Modifier,
) {
  LazyColumn(
    modifier = modifier.fillMaxSize(),
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
        style = MaterialTheme.typography.h6,
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = product.description,
        style = MaterialTheme.typography.body2,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
      )
    }
  }
}
