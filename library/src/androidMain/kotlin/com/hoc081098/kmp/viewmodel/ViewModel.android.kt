package com.hoc081098.kmp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope as androidXViewModelScope
import kotlinx.coroutines.CoroutineScope

public actual abstract class ViewModel : ViewModel {
  public actual constructor() : super()

  public actual constructor(vararg closeables: Closeable) : super(*closeables)

  protected actual val viewModelScope: CoroutineScope get() = androidXViewModelScope

  protected actual override fun onCleared(): Unit = super.onCleared()

  actual override fun addCloseable(closeable: Closeable): Unit = super.addCloseable(closeable)
}
