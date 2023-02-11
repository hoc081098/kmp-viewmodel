package com.hoc081098.kmpviewmodelsample

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

private val commonModule = module {
  factory { GetProducts() }
}

internal expect val platformModule: Module

internal expect fun setupNapier()

fun startKoinCommon(
  appDeclaration: KoinAppDeclaration = {},
) {
  setupNapier()

  startKoin {
    appDeclaration()
    modules(commonModule, platformModule)
  }
}
