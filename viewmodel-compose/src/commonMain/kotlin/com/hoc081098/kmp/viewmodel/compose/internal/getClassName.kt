package com.hoc081098.kmp.viewmodel.compose.internal

import com.hoc081098.kmp.viewmodel.InternalKmpViewModelApi
import kotlin.jvm.JvmSynthetic
import kotlin.reflect.KClass

@InternalKmpViewModelApi
@JvmSynthetic
public expect fun <T : Any> getCanonicalName(kClass: KClass<T>): String?
