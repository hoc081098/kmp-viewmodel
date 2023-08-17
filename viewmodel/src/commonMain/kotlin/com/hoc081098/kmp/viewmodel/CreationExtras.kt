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
public expect object EmptyCreationExtras : CreationExtras

/**
 * Mutable implementation of [CreationExtras]
 *
 * @param initialExtras extras that will be filled into the resulting [MutableCreationExtras]
 */
public expect class MutableCreationExtras(initialExtras: CreationExtras = EmptyCreationExtras) : CreationExtras {
  /**
   * Associates the given [key] with [t]
   */
  public operator fun <T> set(key: Key<T>, t: T)
}

public expect val VIEW_MODEL_KEY: Key<String>
