package com.hoc081098.kmpviewmodelsample.products

import com.hoc081098.kmpviewmodelsample.FakeProductsJson
import com.hoc081098.kmpviewmodelsample.ProductItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class GetProducts {
  private val mutex = Mutex()
  private var i = 0

  suspend operator fun invoke(): List<ProductItem> = mutex.withLock {
    @Suppress("MagicNumber")
    delay(2_000)

    if (i++ % 2 == 0) {
      @Suppress("TooGenericExceptionThrown")
      throw RuntimeException("Fake error")
    }

    return Json.decodeFromString<List<ProductItem>>(FakeProductsJson).shuffled()
  }
}
