package com.hoc081098.kmp.viewmodel.wrapper

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow

/**
 * Same as [NullableFlowWrapper] but wraps and implements [StateFlow] and exposes the current value.
 */
public class NullableStateFlowWrapper<T>
@OptIn(ExperimentalObjCName::class)
constructor(
  @ObjCName("_")
  private val flow: StateFlow<T>,
) :
  NullableFlowWrapper<T>(flow),
  StateFlow<T> by flow {
  override suspend fun collect(collector: FlowCollector<T>): Nothing = flow.collect(collector)
}

public fun <T> StateFlow<T>.wrap(): NullableStateFlowWrapper<T> =
  this as? NullableStateFlowWrapper<T> ?: NullableStateFlowWrapper(this)
