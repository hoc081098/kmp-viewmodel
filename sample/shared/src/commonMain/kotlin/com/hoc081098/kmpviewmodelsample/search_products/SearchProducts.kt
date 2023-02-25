@file:Suppress("PackageNaming")

package com.hoc081098.kmpviewmodelsample.search_products

import com.hoc081098.kmpviewmodelsample.FakeProductsJson
import com.hoc081098.kmpviewmodelsample.ProductItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class SearchProducts {
  private val mutex = Mutex()
//  private var i = 0

  suspend operator fun invoke(term: String): List<ProductItem> = mutex.withLock {
    @Suppress("MagicNumber")
    (delay(2_000))

//    if (i++ % 2 == 0) {
//      @Suppress("TooGenericExceptionThrown")
//      throw RuntimeException("Fake error")
//    }

    return Json.decodeFromString<List<ProductItem>>(FakeProductsJson)
      .filter {
        it.title.contains(term, ignoreCase = true) ||
          it.description.contains(term, ignoreCase = true)
      }
  }
}
