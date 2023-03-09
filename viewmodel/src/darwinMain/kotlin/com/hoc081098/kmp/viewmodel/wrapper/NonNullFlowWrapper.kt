package com.hoc081098.kmp.viewmodel.wrapper

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

/**
 * A wrapper for [Flow] that provides a more convenient API for subscribing to the flow
 * when using Flow on native platforms.
 *
 * @param T the type of the flow's values, it is non-nullable.
 */
@Suppress("RedundantOverride")
// TODO: Override to make generated Objective-C code has the non-optional generic parameter.
public class NonNullFlowWrapper<out T : Any>(flow: Flow<T>) : FlowWrapper<T>(flow) {
  override fun subscribe(
    scope: CoroutineScope,
    onValue: (value: T) -> Unit,
    onError: (throwable: Throwable) -> Unit,
    onComplete: () -> Unit,
  ): JoinableAndCloseable = super.subscribe(scope, onValue, onError, onComplete)

  override fun subscribe(scope: CoroutineScope, onValue: (value: T) -> Unit): JoinableAndCloseable =
    super.subscribe(scope, onValue)

  override fun subscribe(
    scope: CoroutineScope,
    onValue: (value: T) -> Unit,
    onError: (throwable: Throwable) -> Unit,
  ): JoinableAndCloseable = super.subscribe(scope, onValue, onError)

  override fun subscribe(
    scope: CoroutineScope,
    onValue: (value: T) -> Unit,
    onComplete: () -> Unit,
  ): JoinableAndCloseable = super.subscribe(scope, onValue, onComplete)
}
