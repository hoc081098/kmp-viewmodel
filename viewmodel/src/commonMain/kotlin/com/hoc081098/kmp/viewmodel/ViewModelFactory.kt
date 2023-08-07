package com.hoc081098.kmp.viewmodel

public interface ViewModelFactory<VM : ViewModel> {
  public fun create(extras: CreationExtras): VM
}

public inline fun <VM : ViewModel> viewModelFactory(
  crossinline builder: CreationExtras.() -> VM,
): ViewModelFactory<VM> = object : ViewModelFactory<VM> {
  override fun create(extras: CreationExtras): VM = builder(extras)
}
