package com.hoc081098.kmp.viewmodel.utils

actual suspend fun runBlockInNewThread(block: () -> Unit) = block()
