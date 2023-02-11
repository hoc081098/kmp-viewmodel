package com.hoc081098.kmpviewmodelsample

import kotlinx.coroutines.delay
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class GetProducts {
  suspend operator fun invoke(): List<ProductItem> {
    @Suppress("MagicNumber")
    delay(2_000)
    return Json.decodeFromString(FakeProductsJson)
  }
}
