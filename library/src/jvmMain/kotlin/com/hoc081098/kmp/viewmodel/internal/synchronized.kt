package com.hoc081098.kmp.viewmodel.internal

import com.hoc081098.kmp.viewmodel.Lockable

internal actual inline fun <R> synchronized(lock: Lockable, block: () -> R): R = kotlin.synchronized(lock, block)
