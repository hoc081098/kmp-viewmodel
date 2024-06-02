/*
 * Copyright 2021 The Android Open Source Project
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

import kotlin.jvm.JvmField

public actual interface CreationExtrasKey<T>

public actual abstract class CreationExtras internal actual constructor() {
  internal val map: MutableMap<Key<*>, Any?> = mutableMapOf()

  public actual abstract operator fun <T> get(key: Key<T>): T?
}

public actual object EmptyCreationExtras : CreationExtras() {
  actual override fun <T> get(key: Key<T>): T? = null
}

public actual class MutableCreationExtrasBuilder public actual constructor(initialExtras: CreationExtras) :
  CreationExtras() {
  init {
    map.putAll(initialExtras.map)
  }

  /**
   * Associates the given [key] with [t]
   */
  public actual operator fun <T> set(key: Key<T>, t: T) {
    map[key] = t
  }

  public actual override fun <T> get(key: Key<T>): T? {
    @Suppress("UNCHECKED_CAST")
    return map[key] as T?
  }

  public actual fun asCreationExtras(): CreationExtras = this
}

@JvmField
public actual val VIEW_MODEL_KEY: Key<String> = object : Key<String> {}
