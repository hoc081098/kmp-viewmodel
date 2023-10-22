@file:Suppress("PackageNaming")

package com.hoc081098.kmpviewmodelsample.android.product_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.hoc081098.kmpviewmodelsample.ProductItemUi
import com.hoc081098.kmpviewmodelsample.android.common.ErrorMessageAndRetryButton
import com.hoc081098.kmpviewmodelsample.android.common.LoadingIndicator
import com.hoc081098.kmpviewmodelsample.android.common.OnLifecycleEventWithBuilder
import com.hoc081098.kmpviewmodelsample.product_detail.ProductDetailState
import com.hoc081098.kmpviewmodelsample.product_detail.ProductDetailViewModel
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.persistentListOf
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductDetailScreen(
  modifier: Modifier = Modifier,
  viewModel: ProductDetailViewModel = koinViewModel(),
) {
  val refresh = remember(viewModel) {
    @Suppress("SuspiciousCallableReferenceInLambda")
    viewModel::refresh
  }

  OnLifecycleEventWithBuilder(refresh) {
    onResume { refresh() }
    onPause { Napier.d("[ProductDetailScreen] paused") }
    onEach { owner, event -> Napier.d("[ProductDetailScreen] event=$event, owner=$owner") }
  }

  val state by viewModel.stateFlow.collectAsStateWithLifecycle()
  when (val s = state) {
    is ProductDetailState.Error -> {
      ErrorMessageAndRetryButton(
        modifier = modifier,
        onRetry = viewModel::retry,
        errorMessage = s.error.message ?: "Unknown error",
      )
    }

    ProductDetailState.Loading -> {
      LoadingIndicator(modifier = modifier)
    }

    is ProductDetailState.Success -> {
      ProductDetailContent(
        modifier = modifier,
        product = s.product,
      )
    }
  }
}

@Composable
private fun ProductDetailContent(
  product: ProductItemUi,
  modifier: Modifier = Modifier,
) {
  val pairs = remember(product) {
    persistentListOf(
      "Title: " to product.title,
      "Price: " to product.price.toString(),
      "Description: " to product.description,
      "Category: " to product.category.name,
      "Created at: " to product.creationAt,
      "Updated at: " to product.updatedAt,
    )
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 24.dp),
  ) {
    SubcomposeAsyncImage(
      model = product.images.firstOrNull(),
      contentDescription = product.title,
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp)
        .aspectRatio(1f)
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

    pairs.forEach { (title, content) ->
      SimpleTile(
        title = title,
        content = content,
        modifier = Modifier
          .padding(vertical = 8.dp),
      )
      Spacer(modifier = Modifier.height(8.dp))
    }
  }
}

@Composable
private fun SimpleTile(
  title: String,
  content: String,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier
      .fillMaxWidth(),
  ) {
    Text(
      text = title,
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.Start,
    )

    Spacer(modifier = Modifier.width(8.dp))

    Text(
      modifier = Modifier
        .weight(1f),
      text = content,
      style = MaterialTheme.typography.bodyMedium,
      fontWeight = FontWeight.Normal,
      textAlign = TextAlign.End,
    )
  }
}
