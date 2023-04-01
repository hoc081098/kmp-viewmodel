@file:Suppress("SpellCheckingInspection")

package com.hoc081098.kmp.viewmodel.wrapper

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

internal typealias OnValue<T> = (value: T) -> Unit
internal typealias OnError = (throwable: Throwable) -> Unit
internal typealias OnComplete = () -> Unit

/**
 * RxJava-like subscribe for Kotlin Flow.
 */
internal fun <T> Flow<T>.subscribe(
  scope: CoroutineScope,
  onValue: OnValue<T>,
  onError: OnError?,
  onComplete: OnComplete?,
): JoinableAndCloseable {
  val job = this
    .onEach(onValue)
    .run {
      if (onComplete !== null) {
        onCompletion { if (it === null) onComplete() else throw it }
      } else {
        this
      }
    }
    .run {
      if (onError !== null) {
        catch { onError(it) }
      } else {
        this
      }
    }
    .launchIn(scope)

  return JoinableAndCloseableJobImpl(job)
}

private class JoinableAndCloseableJobImpl(private val job: Job) : JoinableAndCloseable {
  override fun close() = job.cancel()
  override suspend fun join() = job.join()
}
