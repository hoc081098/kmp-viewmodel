package com.hoc081098.kmp.viewmodel.compose.internal

import com.hoc081098.kmp.viewmodel.InternalKmpViewModelApi
import kotlin.jvm.JvmSynthetic
import kotlin.reflect.KClass

// TODO: Use reified type parameter when KT-57727 is fixed
// TODO: Use `T::class` when https://youtrack.jetbrains.com/issue/KT-57727 is fixed
// TODO: Use `T::class` when https://github.com/JetBrains/compose-multiplatform/issues/3147
@InternalKmpViewModelApi
@JvmSynthetic
public inline fun <reified T : Any> kClassOf(): KClass<T> = T::class
