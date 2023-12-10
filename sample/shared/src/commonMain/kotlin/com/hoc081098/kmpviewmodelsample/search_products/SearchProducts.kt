@file:Suppress("PackageNaming")

package com.hoc081098.kmpviewmodelsample.search_products

import com.hoc081098.kmpviewmodelsample.common.AppDispatchers
import com.hoc081098.kmpviewmodelsample.data.FakeProductsJson
import com.hoc081098.kmpviewmodelsample.data.ProductItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class SearchProducts(
  private val appDispatchers: AppDispatchers,
) {
  private val mutex = Mutex()

  suspend operator fun invoke(term: String): List<ProductItem> = mutex.withLock {
    withContext(appDispatchers.io) {
      @Suppress("MagicNumber")
      (delay(2_000))

      Json.decodeFromString<List<ProductItem>>(FakeProductsJson)
        .filter {
          it.title.contains(term, ignoreCase = true) ||
            it.description.contains(term, ignoreCase = true)
        }
    }
  }
}
