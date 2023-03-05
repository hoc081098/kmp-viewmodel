package com.hoc081098.kmp.viewmodel.wrapper

import com.hoc081098.kmp.viewmodel.Closeable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

internal fun <T> Flow<T>.subscribe(
  scope: CoroutineScope,
  onValue: (value: T) -> Unit,
  onError: ((throwable: Throwable) -> Unit)? = null,
  onComplete: (() -> Unit)? = null,
): Closeable {
  val job = this
    .onEach(onValue)
    .run {
      if (onComplete !== null) {
        onCompletion { if (it === null) onComplete() }
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

  return Closeable { job.cancel() }
}
