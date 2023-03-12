package com.hoc081098.kmp.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext

@OptIn(DelicateCoroutinesApi::class)
actual suspend fun runBlockInNewThread(block: () -> Unit) {
  newSingleThreadContext("runBlockInNewThread").use { dispatcher ->
    CoroutineScope(dispatcher).run {
      launch { block() }.join()
      coroutineContext[Job]!!.cancelAndJoin()
    }
  }
}
