package com.hoc081098.kmp.viewmodel

import androidx.lifecycle.ViewModelStore as AndroidXViewModelStore

@OptIn(InternalKmpViewModelApi::class)
public fun ViewModelStore.toAndroidX(): AndroidXViewModelStore = this.platform

@OptIn(InternalKmpViewModelApi::class)
public fun AndroidXViewModelStore.toKmp(): ViewModelStore = ViewModelStore(this)

public actual open class ViewModelStore
@InternalKmpViewModelApi public constructor(
  @InternalKmpViewModelApi
  @JvmField public val platform: AndroidXViewModelStore,
) {

  /**
   * @hide
   */
  @Suppress("RestrictedApi")
  @InternalKmpViewModelApi
  public actual fun put(key: String, viewModel: ViewModel) {
    platform.put(key, viewModel)
  }

  /**
   * @hide
   */
  @Suppress("RestrictedApi")
  @InternalKmpViewModelApi
  public actual operator fun get(key: String): ViewModel? {
    return platform[key] as ViewModel?
  }

  /**
   * @hide
   */
  @Suppress("RestrictedApi")
  @InternalKmpViewModelApi
  public actual fun keys(): Set<String> {
    return platform.keys()
  }

  @OptIn(InternalKmpViewModelApi::class)
  public actual fun clear() {
    platform.clear()
  }
}
