package com.hoc081098.kmpviewmodelsample.common

internal expect class WeakReference<T : Any> constructor(reference: T) {
  fun get(): T?
}
