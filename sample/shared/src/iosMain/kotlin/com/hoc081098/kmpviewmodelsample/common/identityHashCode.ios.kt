package com.hoc081098.kmpviewmodelsample.common

import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.identityHashCode as nativeIdentityHashCode

@OptIn(ExperimentalNativeApi::class)
internal actual fun Any?.identityHashCode(): Int = nativeIdentityHashCode()
