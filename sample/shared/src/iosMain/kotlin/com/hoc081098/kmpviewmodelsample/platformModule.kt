package com.hoc081098.kmpviewmodelsample

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal actual val platformModule: Module = module {
  factoryOf(::ProductsViewModel)
}

actual fun setupNapier() {
  Napier.base(DebugAntilog())
}
