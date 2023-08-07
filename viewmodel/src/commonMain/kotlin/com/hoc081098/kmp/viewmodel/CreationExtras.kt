package com.hoc081098.kmp.viewmodel

public expect interface CreationExtrasKey<T>

internal typealias Key<T> = CreationExtrasKey<T>

public expect abstract class CreationExtras internal constructor() {
  public abstract operator fun <T> get(key: Key<T>): T?
}

public expect object EmptyCreationExtras : CreationExtras

public expect class MutableCreationExtras(initialExtras: CreationExtras = EmptyCreationExtras) : CreationExtras {
  public operator fun <T> set(key: Key<T>, t: T)
}
