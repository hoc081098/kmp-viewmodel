package com.hoc081098.kmp.viewmodel

public fun creationExtrasOf(vararg pairs: Pair<Key<Any?>, Any?>): MutableCreationExtras =
  MutableCreationExtras().apply {
    for ((key, value) in pairs) {
      this[key] = value
    }
  }

public fun buildCreationExtras(
  initialExtras: CreationExtras = EmptyCreationExtras,
  builderAction: MutableCreationExtras.() -> Unit,
): CreationExtras = MutableCreationExtras(initialExtras).apply(builderAction)

public operator fun CreationExtras.plus(other: CreationExtras): CreationExtras = when {
  this is EmptyCreationExtras -> other
  other is EmptyCreationExtras -> this
  else -> CombinedCreationExtras(this, other)
}

private class CombinedCreationExtras(
  private val left: CreationExtras,
  private val right: CreationExtras,
) : CreationExtras() {
  override fun <T> get(key: Key<T>): T? = left[key] ?: right[key]
}
