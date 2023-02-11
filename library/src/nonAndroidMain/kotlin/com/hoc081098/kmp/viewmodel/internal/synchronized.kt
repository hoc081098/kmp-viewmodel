package com.hoc081098.kmp.viewmodel.internal

import com.hoc081098.kmp.viewmodel.Lockable

internal expect inline fun <R> synchronized(lock: Lockable, block: () -> R): R
