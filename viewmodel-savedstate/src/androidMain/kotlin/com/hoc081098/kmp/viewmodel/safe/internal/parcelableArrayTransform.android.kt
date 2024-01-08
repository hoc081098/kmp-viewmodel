package com.hoc081098.kmp.viewmodel.safe.internal

import com.hoc081098.kmp.viewmodel.parcelable.Parcelable

@JvmSynthetic
@PublishedApi
internal actual inline fun <reified T : Parcelable> parcelableArrayTransform(): ((Any?) -> Array<T?>)? {
  return transform@{ value ->
    // Workaround according to https://github.com/androidx/androidx/commit/2ffce096e13e3aa4675a8b0fd8b0d74cb1ced653
    //
    // From AndroidX's commit message:
    //
    // As part of the exploration of this problem, arrays
    // of Parcelables were found impossible to actually
    // properly support out of the box, so additional KDocs
    // were added to specifically call out the workaround
    // required to support those.

    value!!

    @Suppress("UNCHECKED_CAST")
    value as Array<Parcelable?>

    Array(value.size) { value[it] as T? }
  }
}
