package com.hoc081098.kmp.viewmodel.safe.internal

import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * Map a [StateFlow] to another [StateFlow] with the given [transform] function.
 */
internal fun <T, R> StateFlow<T>.mapState(transform: (T) -> R): StateFlow<R> =
  object : StateFlow<R> {
    override val replayCache: List<R>
      get() = this@mapState.replayCache.map(transform)

    override val value: R
      get() = transform(this@mapState.value)

    override suspend fun collect(collector: FlowCollector<R>): Nothing {
      this@mapState
        .map { transform(it) }
        .distinctUntilChanged()
        .collect(collector)

      error("StateFlow collection never ends.")
    }
  }
