package com.hoc081098.kmp.viewmodel

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual fun viewModelScopeDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate