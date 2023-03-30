package com.hoc081098.kmp.viewmodel.wrapper

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName
import kotlinx.coroutines.flow.Flow

/**
 * A wrapper for [Flow] that provides a more convenient API for subscribing to the flow
 * when using Flow on native platforms.
 *
 * @param T the type of the flow's values, it is nullable.
 */
public open class NullableFlowWrapper<out T : Any?>
@OptIn(ExperimentalObjCName::class)
constructor(
  @ObjCName("_")
  flow: Flow<T>,
) : AbstractFlowWrapper<T>(flow)

public fun <T> Flow<T>.wrap(): NullableFlowWrapper<T> =
  this as? NullableFlowWrapper<T> ?: NullableFlowWrapper(this)
