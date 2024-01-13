package com.hoc081098.kmp.viewmodel

import kotlin.reflect.KClass

/**
 * Implementations of `Factory` interface are responsible to instantiate [ViewModel]s.
 */
public interface ViewModelFactory<VM : ViewModel> {
  public val viewModelClass: KClass<VM>

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
public inline fun <reified VM : ViewModel> viewModelFactory(
  crossinline builder: CreationExtras.() -> VM,
): ViewModelFactory<VM> = object : ViewModelFactory<VM> {
  override val viewModelClass: KClass<VM> = VM::class
  override fun create(extras: CreationExtras): VM = builder(extras)
}

/**
 * Creates a [ViewModelFactory] that returns the result of invoking [builder].
 */
public inline fun <VM : ViewModel> viewModelFactory(
  viewModelClass: KClass<VM>,
  crossinline builder: CreationExtras.() -> VM,
): ViewModelFactory<VM> = object : ViewModelFactory<VM> {
  override val viewModelClass: KClass<VM> = viewModelClass
  override fun create(extras: CreationExtras): VM = builder(extras)
}
