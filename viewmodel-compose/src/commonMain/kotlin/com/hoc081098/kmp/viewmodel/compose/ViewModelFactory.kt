package com.hoc081098.kmp.viewmodel.compose

import com.hoc081098.kmp.viewmodel.ViewModel

public sealed interface ViewModelFactory<VM : ViewModel> {
  public fun create(): VM
}

internal class DefaultViewModelFactory<VM : ViewModel>(
  private val builder: () -> VM,
) : ViewModelFactory<VM> {
  override fun create(): VM = builder()
}

public fun <VM : ViewModel> viewModelFactory(builder: () -> VM): ViewModelFactory<VM> =
  DefaultViewModelFactory(builder)
