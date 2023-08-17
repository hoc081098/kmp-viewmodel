package com.hoc081098.kmp.viewmodel

public fun creationExtrasOf(vararg pairs: Pair<Key<Any?>, Any?>): MutableCreationExtras =
  MutableCreationExtras().apply {
    for ((key, value) in pairs) {
      this[key] = value
    }
  }

public inline fun buildCreationExtras(
  initialExtras: CreationExtras = EmptyCreationExtras,
  builderAction: MutableCreationExtras.() -> Unit,
): CreationExtras = MutableCreationExtras(initialExtras).apply(builderAction)

public inline fun CreationExtras.edit(
  builderAction: MutableCreationExtras.() -> Unit,
): CreationExtras = buildCreationExtras(this, builderAction)
