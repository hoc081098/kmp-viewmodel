package com.hoc081098.kmpviewmodelsample

import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmpviewmodelsample.product_detail.ProductDetailViewModel
import com.hoc081098.kmpviewmodelsample.products.ProductsViewModel
import com.hoc081098.kmpviewmodelsample.search_products.SearchProductsViewModel
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

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
  Napier.base(DebugAntilog())
}

internal class IosAppDispatchers : AppDispatchers {
  override val main get() = Dispatchers.Main
  override val immediateMain get() = Dispatchers.Main.immediate
  override val io get() = Dispatchers.IO
  override val default get() = Dispatchers.Default
}
