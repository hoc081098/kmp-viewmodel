package com.hoc081098.kmp.viewmodel

import androidx.lifecycle.ViewModel as AndroidXViewModel
import androidx.lifecycle.viewModelScope as androidXViewModelScope
import kotlinx.coroutines.CoroutineScope

public actual abstract class ViewModel : AndroidXViewModel {
  public actual constructor() : super()

  @Suppress("SpreadOperator")
  public actual constructor(vararg closeables: Closeable) : super(*closeables)

  public actual val viewModelScope: CoroutineScope get() = androidXViewModelScope

  protected actual override fun onCleared(): Unit = super.onCleared()

  actual override fun addCloseable(closeable: Closeable): Unit = super.addCloseable(closeable)
}
