@file:Suppress("unused") // Called from platform code
@file:OptIn(ExperimentalNativeApi::class, BetaInteropApi::class)

package com.hoc081098.kmpviewmodelsample

import kotlin.experimental.ExperimentalNativeApi
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.ObjCObject
import kotlinx.cinterop.ObjCProtocol
import kotlinx.cinterop.getOriginalKotlinClass
import org.koin.core.component.KoinComponent
import org.koin.core.logger.Level
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.ParametersHolder
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.KoinAppDeclaration

object DIContainer : KoinComponent {
  fun init(appDeclaration: KoinAppDeclaration = {}) {
    setupNapier()
    startKoinCommon {
      appDeclaration()
      printLogger(
        if (Platform.isDebugBinary) {
          Level.DEBUG
        } else {
          Level.ERROR
        },
      )
    }
  }

  fun get(
    type: ObjCObject,
    qualifier: Qualifier? = null,
    parameters: ParametersDefinition? = null,
  ): Any? = getKoin().get(
    clazz = when (type) {
      is ObjCProtocol -> getOriginalKotlinClass(type)!!
      is ObjCClass -> getOriginalKotlinClass(type)!!
      else -> error("Cannot convert $type to KClass<*>")
    },
    qualifier = qualifier,
    parameters = parameters,
  )

  fun parametersOf(parameters: List<Any?>): ParametersHolder = ParametersHolder(parameters.toMutableList())
}
