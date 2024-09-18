package com.hoc081098.kmp.viewmodel.compose.internal

import com.hoc081098.kmp.viewmodel.InternalKmpViewModelApi
import com.hoc081098.kmp.viewmodel.compose.canonicalName
import kotlin.jvm.JvmSynthetic
import kotlin.reflect.KClass

@JvmSynthetic
@InternalKmpViewModelApi
public actual fun <T : Any> getCanonicalName(kClass: KClass<T>): String? =
  kClass.canonicalName
