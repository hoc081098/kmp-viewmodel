package com.hoc081098.kmp.viewmodel

import java.util.concurrent.Executors
import kotlinx.coroutines.suspendCancellableCoroutine

actual suspend fun runBlockInNewThread(block: () -> Unit) = suspendCancellableCoroutine { cont ->
  val executor = Executors.newSingleThreadExecutor()

  cont.invokeOnCancellation { executor.shutdown() }

  executor.execute {
    if (cont.isActive) {
      cont.resumeWith(runCatching(block))
      executor.shutdown()
    }
  }
}
