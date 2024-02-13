package com.hoc081098.kmp.viewmodel.safe

import androidx.annotation.CheckResult
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * @see [androidx.lifecycle.SavedStateHandle.getLiveData]
 */
@OptIn(DelicateSafeSavedStateHandleApi::class)
public fun <T : Any> SafeSavedStateHandle.getLiveData(key: NonNullSavedStateHandleKey<T>): LiveData<T> {
  val transform = key.transform

  return if (transform == null) {
    @Suppress("RemoveExplicitTypeArguments") // Readability
    savedStateHandle
      .getLiveData<T>(key.key, key.defaultValue)
  } else {
    savedStateHandle
      .getLiveData<Any>(key.key, key.defaultValue)
      .map(transform)
  }.also { check(it.isInitialized) { "LiveData isn't initialized" } }
}

/**
 * @see [androidx.lifecycle.SavedStateHandle.getLiveData]
 */
@OptIn(DelicateSafeSavedStateHandleApi::class)
public fun <T : Any> SafeSavedStateHandle.getLiveData(key: NullableSavedStateHandleKey<T>): LiveData<T?> {
  val transform = key.transform

  return if (transform == null) {
    @Suppress("RemoveExplicitTypeArguments") // Readability
    savedStateHandle
      .getLiveData<T?>(key.key, key.defaultValue)
  } else {
    savedStateHandle
      .getLiveData<Any?>(key.key, key.defaultValue)
      .map(transform)
  }.also { check(it.isInitialized) { "LiveData isn't initialized" } }
}

@MainThread
@CheckResult
private fun <X, Y> LiveData<X>.map(
  transform: (@JvmSuppressWildcards X) -> (@JvmSuppressWildcards Y),
): LiveData<Y> = MapLiveData(this, transform)

private class MapLiveData<X, Y>(
  private val source: LiveData<X>,
  private val transform: (@JvmSuppressWildcards X) -> @JvmSuppressWildcards Y,
) : LiveData<Y>() {
  private val observer = Observer<X> { value = transform(it) }

  override fun isInitialized(): Boolean = true

  @Suppress("UNCHECKED_CAST")
  override fun getValue(): Y? = transform(source.value as X)

  override fun onActive() {
    super.onActive()
    source.observeForever(observer)
  }

  override fun onInactive() {
    source.removeObserver(observer)
    super.onInactive()
  }
}
