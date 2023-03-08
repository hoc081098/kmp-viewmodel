package com.hoc081098.kmp.viewmodel.wrapper

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

/**
 * A wrapper for [Flow] that provides a more convenient API for subscribing to the flow
 * when using Flow on native platforms.
 */
// FIXME(overloads): https://youtrack.jetbrains.com/issue/KT-38685/Generate-overloaded-obj-c-functions-for-functions-with-default-parameter-values
public abstract class FlowWrapper<out T>(private val flow: Flow<T>) : Flow<T> by flow {
  public fun subscribe(
    scope: CoroutineScope,
    onValue: (value: T) -> Unit,
    onError: (throwable: Throwable) -> Unit,
    onComplete: () -> Unit,
  ): JoinableAndCloseable = flow.subscribe(
    scope = scope,
    onValue = onValue,
    onError = onError,
    onComplete = onComplete,
  )

  public fun subscribe(
    scope: CoroutineScope,
    onValue: (value: T) -> Unit,
  ): JoinableAndCloseable = flow.subscribe(
    scope = scope,
    onValue = onValue,
    onError = null,
    onComplete = null,
  )

  public fun subscribe(
    scope: CoroutineScope,
    onValue: (value: T) -> Unit,
    onError: (throwable: Throwable) -> Unit,
  ): JoinableAndCloseable = flow.subscribe(
    scope = scope,
    onValue = onValue,
    onError = onError,
    onComplete = null,
  )

  public fun subscribe(
    scope: CoroutineScope,
    onValue: (value: T) -> Unit,
    onComplete: () -> Unit,
  ): JoinableAndCloseable = flow.subscribe(
    scope = scope,
    onValue = onValue,
    onError = null,
    onComplete = onComplete,
  )
}
