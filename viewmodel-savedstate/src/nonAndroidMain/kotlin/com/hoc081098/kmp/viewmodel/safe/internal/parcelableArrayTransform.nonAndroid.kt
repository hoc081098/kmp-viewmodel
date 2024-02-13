package com.hoc081098.kmp.viewmodel.safe.internal

import com.hoc081098.kmp.viewmodel.parcelable.Parcelable
import kotlin.jvm.JvmSynthetic

@JvmSynthetic
@PublishedApi
internal actual inline fun <reified T : Parcelable> parcelableArrayTransform(): ((Any?) -> Array<T?>)? = null
