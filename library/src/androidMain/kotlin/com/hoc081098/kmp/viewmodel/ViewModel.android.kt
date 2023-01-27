package com.hoc081098.kmp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope as androidXViewModelScope
import kotlinx.coroutines.CoroutineScope

public actual abstract class ViewModel public actual constructor() : ViewModel() {
  protected actual val viewModelScope: CoroutineScope get() = androidXViewModelScope

  protected actual override fun onCleared(): Unit = super.onCleared()
}
