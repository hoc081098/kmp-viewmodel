package com.hoc081098.kmpviewmodelsample

import com.hoc081098.kmpviewmodelsample.products.ProductsViewModel
import com.hoc081098.kmpviewmodelsample.search_products.SearchProductsViewModel
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformModule: Module = module {
  viewModelOf(::ProductsViewModel)

  viewModelOf(::SearchProductsViewModel)
}

actual fun setupNapier() {
  if (BuildConfig.DEBUG) {
    Napier.base(DebugAntilog())
  }
}
