package com.hoc081098.kmp.viewmodel

expect suspend fun runBlockInNewThread(block: () -> Unit)
