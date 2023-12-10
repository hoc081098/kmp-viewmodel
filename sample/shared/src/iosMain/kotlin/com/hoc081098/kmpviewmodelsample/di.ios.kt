package com.hoc081098.kmpviewmodelsample

import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmpviewmodelsample.common.AppDispatchers
import com.hoc081098.kmpviewmodelsample.common.IosAppDispatchers
import com.hoc081098.kmpviewmodelsample.product_detail.ProductDetailViewModel
import com.hoc081098.kmpviewmodelsample.products.ProductsViewModel
import com.hoc081098.kmpviewmodelsample.search_products.SearchProductsViewModel
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlin.experimental.ExperimentalNativeApi
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

@OptIn(ExperimentalNativeApi::class)
actual fun isDebug(): Boolean = Platform.isDebugBinary

internal actual val PlatformModule: Module = module {
  factoryOf(::ProductsViewModel)
  factoryOf(::SearchProductsViewModel)
  factory { params ->
    ProductDetailViewModel.create(
      id = params.get(),
      getProductById = get(),
    )
  }
  factory { SavedStateHandle() }
  singleOf(::IosAppDispatchers) { bind<AppDispatchers>() }
}

actual fun setupNapier() {
  if (isDebug()) {
    Napier.base(DebugAntilog())
  }
}
