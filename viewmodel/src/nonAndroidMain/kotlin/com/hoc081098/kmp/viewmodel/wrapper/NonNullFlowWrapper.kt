package com.hoc081098.kmp.viewmodel.wrapper

import kotlinx.coroutines.flow.Flow

/**
 * A wrapper for [Flow] that provides a more convenient API for subscribing to the flow
 * when using Flow on native platforms.
 *
 * @param T the type of the flow's values, it is non-nullable.
 */
public class NonNullFlowWrapper<out T : Any>(flow: Flow<T>) : FlowWrapper<T>(flow)
