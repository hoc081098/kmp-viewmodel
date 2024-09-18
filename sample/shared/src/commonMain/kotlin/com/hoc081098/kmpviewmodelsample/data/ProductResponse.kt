@file:Suppress("MaxLineLength")
package com.hoc081098.kmpviewmodelsample.data

import com.hoc081098.kmpviewmodelsample.domain.ProductItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ProductResponse(
  @SerialName("id") val id: Int, // 1
  @SerialName("title") val title: String, // Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops
  @SerialName("price") val price: Double, // 109.95
  @SerialName("description") val description: String, // Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday
  @SerialName("category") val category: String, // men's clothing
  @SerialName("image") val image: String, // https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg
  @SerialName("rating") val rating: Rating,
) {
  @Serializable
  internal data class Rating(
    @SerialName("rate") val rate: Double, // 3.9
    @SerialName("count") val count: Int, // 120
  )
}

internal fun ProductResponse.toProductItem(): ProductItem = ProductItem(
  id = id,
  title = title,
  price = price,
  description = description,
  category = category,
  image = image,
  rating = ProductItem.Rating(
    rate = rating.rate,
    count = rating.count,
  ),
)
