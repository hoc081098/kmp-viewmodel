package com.hoc081098.kmp.viewmodel

import kotlin.native.concurrent.TransferMode
import kotlin.native.concurrent.withWorker

actual suspend fun runBlockInNewThread(block: () -> Unit) = withWorker {
  execute(TransferMode.SAFE, { block }) { it() }.result
}
