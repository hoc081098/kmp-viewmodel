package com.hoc081098.kmpviewmodelsample

import com.hoc081098.kmpviewmodelsample.products.GetProducts
import com.hoc081098.kmpviewmodelsample.search_products.SearchProducts
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

private val commonModule = module {
  factoryOf(::GetProducts)
  factoryOf(::SearchProducts)
}

internal expect val platformModule: Module

expect fun setupNapier()

fun startKoinCommon(
  appDeclaration: KoinAppDeclaration = {},
) {
  startKoin {
    appDeclaration()
    modules(commonModule, platformModule)
  }
}
