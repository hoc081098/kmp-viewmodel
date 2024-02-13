package com.hoc081098.kmp.viewmodel.internal

import co.touchlab.stately.concurrency.withLock
import com.hoc081098.kmp.viewmodel.Lockable

internal actual inline fun <R> synchronized(lock: Lockable, block: () -> R): R = lock.lock.withLock(block)
