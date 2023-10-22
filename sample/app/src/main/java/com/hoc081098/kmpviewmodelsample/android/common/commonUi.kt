@file:OptIn(ExperimentalMaterialApi::class)

package com.hoc081098.kmpviewmodelsample.android.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.hoc081098.kmpviewmodelsample.ProductItemUi
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun EmptyProducts(modifier: Modifier = Modifier) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    Text(text = "No products")
  }
}

@Composable
internal fun LoadingIndicator(modifier: Modifier = Modifier) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    CircularProgressIndicator()
  }
}

@Composable
internal fun ErrorMessageAndRetryButton(
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

@Suppress("LongParameterList")
@Composable
internal fun ProductItemsList(
  pullRefreshState: PullRefreshState?,
  products: ImmutableList<ProductItemUi>,
  isRefreshing: Boolean,
  onItemClick: (ProductItemUi) -> Unit,
  modifier: Modifier = Modifier,
  lazyListState: LazyListState = rememberLazyListState(),
) {
  Box(
    modifier = modifier,
  ) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .let {
          if (pullRefreshState != null) {
            it.pullRefresh(state = pullRefreshState)
          } else {
            it
          }
        },
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
          onClick = { onItemClick(it) },
        )
      }
    }

    if (pullRefreshState != null) {
      PullRefreshIndicator(
        modifier = Modifier.align(Alignment.TopCenter),
        refreshing = isRefreshing,
        state = pullRefreshState,
        backgroundColor = MaterialTheme.colorScheme.surface,
        contentColor = contentColorFor(backgroundColor = MaterialTheme.colorScheme.surface),
      )
    }
  }
}

@Composable
internal fun ProductItemRow(
  product: ProductItemUi,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier.clickable(onClick = onClick),
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
