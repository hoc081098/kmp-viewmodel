@file:Suppress("SpellCheckingInspection")

package com.hoc081098.kmp.viewmodel.wrapper

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

/**
 * @suppress
 */
internal fun <T> Flow<T>.subscribe(
  scope: CoroutineScope,
  onValue: (value: T) -> Unit,
  onError: ((throwable: Throwable) -> Unit)? = null,
  onComplete: (() -> Unit)? = null,
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
