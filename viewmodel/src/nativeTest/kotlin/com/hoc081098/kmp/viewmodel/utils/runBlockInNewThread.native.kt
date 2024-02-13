package com.hoc081098.kmp.viewmodel.utils

import kotlin.native.concurrent.ObsoleteWorkersApi
import kotlin.native.concurrent.TransferMode
import kotlin.native.concurrent.withWorker

@OptIn(ObsoleteWorkersApi::class)
actual suspend fun runBlockInNewThread(block: () -> Unit) = withWorker {
  execute(TransferMode.SAFE, { block }) { it() }.result
}
