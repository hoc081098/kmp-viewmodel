package com.hoc081098.kmpviewmodelsample.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductItem(
  @SerialName("id") val id: Int, // 3
  @SerialName("title") val title: String, // test product
  @SerialName("price") val price: Int, // 13
  @SerialName("description") val description: String, //  lorem ipsum set
  @SerialName("images") val images: List<String>,
  @SerialName("creationAt") val creationAt: String, // 2023-02-11T02:45:59.000Z
  @SerialName("updatedAt") val updatedAt: String, // 2023-02-11T14:54:14.000Z
  @SerialName("category") val category: Category,
) {
  @Serializable
  data class Category(
    @SerialName("id") val id: Int, // 2
    @SerialName("name") val name: String, // Electronics
    @SerialName("image") val image: String, // https://api.lorem.space/image/watch?w=640&h=480&r=7796
    @SerialName("creationAt") val creationAt: String, // 2023-02-11T02:45:59.000Z
    @SerialName("updatedAt") val updatedAt: String, // 2023-02-11T02:45:59.000Z
  )
}
