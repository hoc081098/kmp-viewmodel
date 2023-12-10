package com.hoc081098.kmpviewmodelsample

import com.hoc081098.kmpviewmodelsample.common.SingleEventChannel
import com.hoc081098.kmpviewmodelsample.product_detail.GetProductById
import com.hoc081098.kmpviewmodelsample.products.GetProducts
import com.hoc081098.kmpviewmodelsample.search_products.SearchProducts
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

private val CommonModule = module {
  factoryOf(::GetProducts)
  factoryOf(::SearchProducts)
  factoryOf(::GetProductById)
  factory { SingleEventChannel<Any?>() }
}

internal expect val PlatformModule: Module

expect fun setupNapier()

expect fun isDebug(): Boolean

fun startKoinCommon(
  appDeclaration: KoinAppDeclaration = {},
) {
  startKoin {
    appDeclaration()
    modules(CommonModule, PlatformModule)
  }
}
