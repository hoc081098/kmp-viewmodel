package com.hoc081098.kmp.viewmodel

/**
 * Implementations of `Factory` interface are responsible to instantiate [ViewModel]s.
 */
public interface ViewModelFactory<VM : ViewModel> {
  /**
   * Creates a new instance of [VM].
   *
   * @param extras an additional information for this creation request
   * @return a newly created ViewModel
   */
  public fun create(extras: CreationExtras): VM
}

/**
 * Creates a [ViewModelFactory] that returns the result of invoking [builder].
 */
public inline fun <VM : ViewModel> viewModelFactory(
  crossinline builder: CreationExtras.() -> VM,
): ViewModelFactory<VM> = object : ViewModelFactory<VM> {
  override fun create(extras: CreationExtras): VM = builder(extras)
}
