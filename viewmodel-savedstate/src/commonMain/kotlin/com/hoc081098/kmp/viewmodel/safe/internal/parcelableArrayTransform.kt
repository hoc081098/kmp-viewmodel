package com.hoc081098.kmp.viewmodel.safe.internal

import com.hoc081098.kmp.viewmodel.parcelable.Parcelable
import kotlin.jvm.JvmSynthetic

@JvmSynthetic
@PublishedApi
internal expect inline fun <reified T : Parcelable> parcelableArrayTransform(): ((Any?) -> Array<T?>)?

@JvmSynthetic
@PublishedApi
internal inline fun <reified T : Parcelable> nullableParcelableArrayTransform(): ((Any?) -> Array<T?>?)? =
  parcelableArrayTransform<T>()
    ?.let { transform ->
      {
        it?.let(transform)
      }
    }
