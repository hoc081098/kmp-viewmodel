package com.hoc081098.kmpviewmodelsample.ui

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.hoc081098.kmpviewmodelsample.domain.ProductItem

@Immutable
data class ProductItemUi(
  val id: Int, // 3
  val title: String, // test product
  val price: Double, // 13
  val description: String, //  lorem ipsum set
  val image: String,
)

@Stable
internal fun ProductItem.toProductItemUi() = ProductItemUi(
  id = id,
  title = title,
  price = price,
  description = description,
  image = image,
)
