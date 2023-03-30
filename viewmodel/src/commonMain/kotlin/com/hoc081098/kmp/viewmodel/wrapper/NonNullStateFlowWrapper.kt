package com.hoc081098.kmp.viewmodel.wrapper

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow

/**
 * Same as [NonNullFlowWrapper] but wraps and implements [StateFlow] and exposes the current value.
 */
public class NonNullStateFlowWrapper<out T : Any>
@OptIn(ExperimentalObjCName::class)
constructor(
  @ObjCName("_")
  private val flow: StateFlow<T>,
) :
  NonNullFlowWrapper<T>(flow),
  StateFlow<T> by flow {
  override suspend fun collect(collector: FlowCollector<T>): Nothing = flow.collect(collector)
}

public fun <T : Any> StateFlow<T>.wrap(): NonNullStateFlowWrapper<T> =
  this as? NonNullStateFlowWrapper<T> ?: NonNullStateFlowWrapper(this)
