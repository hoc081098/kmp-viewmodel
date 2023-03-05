package com.hoc081098.kmp.viewmodel.wrapper

import com.hoc081098.kmp.viewmodel.Closeable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

// FIXME(overloads): https://youtrack.jetbrains.com/issue/KT-38685/Generate-overloaded-obj-c-functions-for-functions-with-default-parameter-values
public class NonNullFlowWrapper<T : Any>(private val flow: Flow<T>) : Flow<T> by flow {
  public fun subscribe(
    scope: CoroutineScope,
    onValue: (value: T) -> Unit,
    onError: (throwable: Throwable) -> Unit,
    onComplete: () -> Unit,
  ): Closeable = flow.subscribe(
    scope = scope,
    onValue = onValue,
    onError = onError,
    onComplete = onComplete,
  )

  public fun subscribe(
    scope: CoroutineScope,
    onValue: (value: T) -> Unit,
  ): Closeable = flow.subscribe(
    scope = scope,
    onValue = onValue,
    onError = null,
    onComplete = null,
  )

  public fun subscribe(
    scope: CoroutineScope,
    onValue: (value: T) -> Unit,
    onError: (throwable: Throwable) -> Unit,
  ): Closeable = flow.subscribe(
    scope = scope,
    onValue = onValue,
    onError = onError,
    onComplete = null,
  )

  public fun subscribe(
    scope: CoroutineScope,
    onValue: (value: T) -> Unit,
    onComplete: () -> Unit,
  ): Closeable = flow.subscribe(
    scope = scope,
    onValue = onValue,
    onError = null,
    onComplete = onComplete,
  )
}
