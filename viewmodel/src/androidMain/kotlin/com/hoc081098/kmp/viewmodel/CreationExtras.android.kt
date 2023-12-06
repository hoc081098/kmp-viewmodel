package com.hoc081098.kmp.viewmodel

import androidx.lifecycle.viewmodel.CreationExtras as AndroidXCreationExtras
import androidx.lifecycle.viewmodel.CreationExtras.Key as AndroidXCreationExtrasKey
import androidx.lifecycle.viewmodel.MutableCreationExtras as AndroidXMutableCreationExtras
import androidx.lifecycle.ViewModelProvider

public actual typealias CreationExtras = AndroidXCreationExtras

public actual typealias CreationExtrasKey<T> = AndroidXCreationExtrasKey<T>

public actual typealias EmptyCreationExtras = AndroidXCreationExtras.Empty

/**
 * With Kotlin 1.9.20, an expect with default arguments are no longer permitted when an actual is a typealias
 * (see [KT-57614](https://youtrack.jetbrains.com/issue/KT-57614)),
 * we cannot use `actual typealias MutableCreationExtras = androidx.lifecycle.viewmodel.MutableCreationExtras`.
 * So we have to use wrapper class instead.
 */
public actual class MutableCreationExtrasBuilder public actual constructor(initialExtras: CreationExtras) {
  private val extras = AndroidXMutableCreationExtras(initialExtras)

  public actual operator fun <T> set(key: Key<T>, t: T) {
    extras[key] = t
  }

  public actual operator fun <T> get(key: Key<T>): T? = extras[key]

  public actual fun asCreationExtras(): CreationExtras = extras
}

/**
 * @see [ViewModelProvider.NewInstanceFactory.VIEW_MODEL_KEY]
 */
public actual val VIEW_MODEL_KEY: Key<String> get() = ViewModelProvider.NewInstanceFactory.VIEW_MODEL_KEY
