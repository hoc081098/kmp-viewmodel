package com.hoc081098.kmpviewmodelsample.common

import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
@Suppress("ACTUAL_WITHOUT_EXPECT") // TODO: https://youtrack.jetbrains.com/issue/KT-37316
internal actual typealias WeakReference<T> = kotlin.native.ref.WeakReference<T>
