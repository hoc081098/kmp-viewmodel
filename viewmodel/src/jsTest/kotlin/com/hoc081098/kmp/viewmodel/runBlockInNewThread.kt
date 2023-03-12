package com.hoc081098.kmp.viewmodel

actual suspend fun runBlockInNewThread(block: () -> Unit) = block()
