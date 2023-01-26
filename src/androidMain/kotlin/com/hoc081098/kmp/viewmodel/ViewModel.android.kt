package com.hoc081098.kmp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import androidx.lifecycle.viewModelScope as androidXViewModelScope

public actual abstract class ViewModel public actual constructor() : ViewModel() {
  protected actual val viewModelScope: CoroutineScope get() = androidXViewModelScope

  protected actual open override fun onCleared() = super.onCleared()
}