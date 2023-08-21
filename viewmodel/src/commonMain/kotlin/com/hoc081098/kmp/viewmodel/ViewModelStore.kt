/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hoc081098.kmp.viewmodel

/**
 * Class to store `ViewModel`s.
 *
 * An instance of `ViewModelStore` must be retained through configuration changes:
 * if an owner of this `ViewModelStore` is destroyed and recreated due to configuration
 * changes, new instance of an owner should still have the same old instance of
 * `ViewModelStore`.
 *
 * If an owner of this `ViewModelStore` is destroyed and is not going to be recreated,
 * then it should call [clear] on this `ViewModelStore`, so `ViewModel`s would
 * be notified that they are no longer used.
 *
 * Use [ViewModelStoreOwner.viewModelStore] to retrieve a `ViewModelStore` for
 * activities and fragments.
 */
public expect open class ViewModelStore {

  /**
   * @hide
   */
  public fun put(key: String, viewModel: ViewModel)

  /**
   * Returns the `ViewModel` mapped to the given `key` or null if none exists.
   */
  /**
   * @hide
   */
  public operator fun get(key: String): ViewModel?

  /**
   * @hide
   */
  public fun keys(): Set<String>

  /**
   * Clears internal storage and notifies `ViewModel`s that they are no longer used.
   */
  public fun clear()
}
