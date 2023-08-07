package com.hoc081098.kmp.viewmodel

public fun creationExtrasOf(vararg pairs: Pair<Key<Any?>, Any?>): MutableCreationExtras =
  MutableCreationExtras().apply {
    for ((key, value) in pairs) {
      this[key] = value
    }
  }

public fun buildCreationExtras(builderAction: MutableCreationExtras.() -> Unit): CreationExtras =
  MutableCreationExtras().apply {
    builderAction()
  }

public operator fun CreationExtras.plus(other: CreationExtras): CreationExtras =
  if (other === EmptyCreationExtras) {
    this
  } else {
    (
      this as? MutableCreationExtras
        ?: MutableCreationExtras(this)
      )
      .apply { map.putAll(other.map) }
  }
