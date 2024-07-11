package com.hoc081098.kmpviewmodelsample.product_detail

import com.hoc081098.kmpviewmodelsample.common.AppDispatchers
import com.hoc081098.kmpviewmodelsample.data.FakeProductsJson
import com.hoc081098.kmpviewmodelsample.data.ProductResponse
import com.hoc081098.kmpviewmodelsample.data.toProductItem
import com.hoc081098.kmpviewmodelsample.domain.ProductItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class GetProductById(
  private val appDispatchers: AppDispatchers,
) {
  suspend operator fun invoke(id: Int): ProductItem = withContext(appDispatchers.io) {
    @Suppress("MagicNumber")
    delay(2_000)

    Json
      .decodeFromString<List<ProductResponse>>(FakeProductsJson)
      .find { it.id == id }
      ?.toProductItem()
      ?:
      @Suppress("TooGenericExceptionThrown")
      throw RuntimeException("Product with id = $id not found")
  }
}
