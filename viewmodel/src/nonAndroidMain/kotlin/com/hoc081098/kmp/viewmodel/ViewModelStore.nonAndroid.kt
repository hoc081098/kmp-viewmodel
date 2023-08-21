package com.hoc081098.kmp.viewmodel

public actual open class ViewModelStore {
  private val map = mutableMapOf<String, ViewModel>()

  /**
   * @hide
   */
  @InternalKmpViewModelApi
  public actual fun put(key: String, viewModel: ViewModel) {
    val oldViewModel = map.put(key, viewModel)
    oldViewModel?.clear()
  }

  /**
   * @hide
   */
  @InternalKmpViewModelApi
  public actual operator fun get(key: String): ViewModel? {
    return map[key]
  }

  /**
   * @hide
   */
  @InternalKmpViewModelApi
  public actual fun keys(): Set<String> {
    return HashSet(map.keys)
  }

  /**
   * Clears internal storage and notifies `ViewModel`s that they are no longer used.
   */
  public actual fun clear() {
    for (vm in map.values) {
      vm.clear()
    }
    map.clear()
  }
}
