package com.hoc081098.kmp.viewmodel.compose

import com.hoc081098.kmp.viewmodel.ViewModel

public interface ViewModelFactory<VM : ViewModel> {
  public fun create(): VM
}

public inline fun <VM : ViewModel> viewModelFactory(
  crossinline builder: () -> VM,
): ViewModelFactory<VM> = object : ViewModelFactory<VM> {
  override fun create(): VM = builder()
}
