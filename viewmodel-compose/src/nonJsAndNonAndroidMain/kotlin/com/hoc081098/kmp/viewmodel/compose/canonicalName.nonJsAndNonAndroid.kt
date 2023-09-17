package com.hoc081098.kmp.viewmodel.compose

import kotlin.reflect.KClass

internal actual inline val <T : Any> KClass<T>.canonicalName: String?
  get() = qualifiedName
