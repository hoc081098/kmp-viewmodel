package com.hoc081098.kmp.viewmodel.wrapper

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName
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
public open class NonNullFlowWrapper<out T : Any>
@OptIn(ExperimentalObjCName::class)
constructor(
  @ObjCName("_")
  flow: Flow<T>,
) : AbstractFlowWrapper<T>(flow) {
  final override fun subscribe(
    scope: CoroutineScope,
    onValue: OnValue<T>,
    onError: OnError,
    onComplete: OnComplete,
  ): JoinableAndCloseable = super.subscribe(scope, onValue, onError, onComplete)

  final override fun subscribe(
    scope: CoroutineScope,
    onValue: OnValue<T>,
  ): JoinableAndCloseable = super.subscribe(scope, onValue)

  final override fun subscribe(
    scope: CoroutineScope,
    onValue: OnValue<T>,
    onError: OnError,
  ): JoinableAndCloseable = super.subscribe(scope, onValue, onError)

  final override fun subscribe(
    scope: CoroutineScope,
    onValue: OnValue<T>,
    onComplete: OnComplete,
  ): JoinableAndCloseable = super.subscribe(scope, onValue, onComplete)
}

public fun <T : Any> Flow<T>.wrap(): NonNullFlowWrapper<T> =
  this as? NonNullFlowWrapper<T> ?: NonNullFlowWrapper(this)
