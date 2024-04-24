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

/**
 * Key for the elements of [CreationExtras]. [T] is a type of an element with this key.
 */
public expect interface CreationExtrasKey<T>

/**
 * Key for the elements of [CreationExtras]. [T] is a type of an element with this key.
 */
internal typealias Key<T> = CreationExtrasKey<T>

/**
 * Simple map-like object that passed in [ViewModelFactory.create]
 * to provide an additional information to a factory.
 *
 * It allows making `Factory` implementations stateless, which makes an injection of factories
 * easier because  don't require all information be available at construction time.
 */
public expect abstract class CreationExtras internal constructor() {

  /**
   * Returns an element associated with the given [key]
   */
  public abstract operator fun <T> get(key: Key<T>): T?
}

/**
 * Empty [CreationExtras].
 *
 * [get] of this object always returns `null`.
 */
public expect object EmptyCreationExtras : CreationExtras {
  public expect operator fun <T> get(key: Key<T>): T?
}

/**
 * Alias for [MutableCreationExtrasBuilder]
 */
@Deprecated(
  message = "Use MutableCreationExtrasBuilder instead",
  replaceWith = ReplaceWith("MutableCreationExtrasBuilder"),
)
public typealias MutableCreationExtras = MutableCreationExtrasBuilder

@Deprecated(
  message = "Use MutableCreationExtrasBuilder(CreationExtras) instead",
  replaceWith = ReplaceWith("MutableCreationExtrasBuilder(initialExtras.asCreationExtras())"),
)
public fun MutableCreationExtrasBuilder(initialExtras: MutableCreationExtrasBuilder): MutableCreationExtrasBuilder =
  MutableCreationExtrasBuilder(initialExtras.asCreationExtras())

/**
 * Mutable builder for [CreationExtras].
 *
 * @param initialExtras extras that will be filled into the resulting [MutableCreationExtrasBuilder]
 */
public expect class MutableCreationExtrasBuilder
constructor(initialExtras: CreationExtras = EmptyCreationExtras) {

  /**
   * Associates the given [key] with [t]
   */
  public operator fun <T> set(key: Key<T>, t: T)

  /**
   * Returns an element associated with the given [key]
   */
  public operator fun <T> get(key: Key<T>): T?

  /**
   * Returns a [CreationExtras] with the same content as this builder.
   */
  public fun asCreationExtras(): CreationExtras
}

/**
 * A [CreationExtrasKey] to get a key associated with a requested
 * `ViewModel` from [CreationExtras].
 */
public expect val VIEW_MODEL_KEY: Key<String>
