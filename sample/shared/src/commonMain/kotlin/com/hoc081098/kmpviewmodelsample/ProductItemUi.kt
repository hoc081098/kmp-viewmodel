package com.hoc081098.kmpviewmodelsample

import com.hoc081098.kmpviewmodelsample.ProductItemUi.CategoryUi
import com.hoc081098.kmpviewmodelsample.common.Immutable
import com.hoc081098.kmpviewmodelsample.data.ProductItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class ProductItemUi(
  val id: Int, // 3
  val title: String, // test product
  val price: Int, // 13
  val description: String, //  lorem ipsum set
  val images: ImmutableList<String>,
  val creationAt: String, // 2023-02-11T02:45:59.000Z
  val updatedAt: String, // 2023-02-11T14:54:14.000Z
  val category: CategoryUi,
) {
  @Immutable
  data class CategoryUi(
    val id: Int, // 2
    val name: String, // Electronics
    val image: String, // https://api.lorem.space/image/watch?w=640&h=480&r=7796
    val creationAt: String, // 2023-02-11T02:45:59.000Z
    val updatedAt: String, // 2023-02-11T02:45:59.000Z
  )
}

internal fun ProductItem.toProductItemUi() = ProductItemUi(
  id = id,
  title = title,
  price = price,
  description = description,
  images = images.toImmutableList(),
  creationAt = creationAt,
  updatedAt = updatedAt,
  category = category.toCategoryUi(),
)

internal fun ProductItem.Category.toCategoryUi(): CategoryUi {
  return CategoryUi(
    id = id,
    name = name,
    image = image,
    creationAt = creationAt,
    updatedAt = updatedAt,
  )
}
