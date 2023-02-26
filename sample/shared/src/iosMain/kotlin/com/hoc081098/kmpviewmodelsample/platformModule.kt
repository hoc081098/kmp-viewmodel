package com.hoc081098.kmpviewmodelsample

import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmpviewmodelsample.products.ProductsViewModel
import com.hoc081098.kmpviewmodelsample.search_products.SearchProductsViewModel
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal actual val platformModule: Module = module {
  factoryOf(::ProductsViewModel)
  factoryOf(::SearchProductsViewModel)
  factory { SavedStateHandle() }
}

actual fun setupNapier() {
  Napier.base(DebugAntilog())
}
