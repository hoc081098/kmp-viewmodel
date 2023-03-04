@file:Suppress("PackageNaming")

package com.hoc081098.kmpviewmodelsample.android.product_detail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hoc081098.kmpviewmodelsample.product_detail.ProductDetailViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductDetailScreen(
  viewModel: ProductDetailViewModel = koinViewModel(),
) {
  val id by viewModel.idStateFlow.collectAsStateWithLifecycle()
  Text(text = "Detail screen: $id")
}
