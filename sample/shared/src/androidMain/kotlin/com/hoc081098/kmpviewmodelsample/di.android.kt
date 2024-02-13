package com.hoc081098.kmpviewmodelsample

import com.hoc081098.kmpviewmodelsample.common.AndroidAppDispatchers
import com.hoc081098.kmpviewmodelsample.common.AppDispatchers
import com.hoc081098.kmpviewmodelsample.product_detail.ProductDetailViewModel
import com.hoc081098.kmpviewmodelsample.products.ProductsViewModel
import com.hoc081098.kmpviewmodelsample.search_products.SearchProductsViewModel
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun isDebug(): Boolean = BuildConfig.DEBUG

internal actual val PlatformModule: Module = module {
  viewModelOf(::ProductsViewModel)
  viewModelOf(::SearchProductsViewModel)
  viewModelOf(::ProductDetailViewModel)
  singleOf(::AndroidAppDispatchers) { bind<AppDispatchers>() }
}

actual fun setupNapier() {
  if (BuildConfig.DEBUG) {
    Napier.base(DebugAntilog())
  }
}
