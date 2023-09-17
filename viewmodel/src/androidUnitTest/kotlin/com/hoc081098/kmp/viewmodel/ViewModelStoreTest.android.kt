package com.hoc081098.kmp.viewmodel

import androidx.lifecycle.ViewModelStore as AndroidXViewModelStore

@OptIn(InternalKmpViewModelApi::class)
actual fun createViewModelStore(): ViewModelStore = ViewModelStore(AndroidXViewModelStore())
