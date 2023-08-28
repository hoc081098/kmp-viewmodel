package com.hoc081098.kmp.viewmodel

/**
 * Create a new [CreationExtras] with the given key/value pairs as the content.
 * If multiple pairs have the same key, the resulting will contain the value from the last of those pairs.
 */
public fun creationExtrasOf(vararg pairs: Pair<Key<Any?>, Any?>): CreationExtras =
  MutableCreationExtras().apply {
    for ((key, value) in pairs) {
      this[key] = value
    }
  }

/**
 * Builds a [CreationExtras] by populating a [MutableCreationExtras] using the given [builderAction].
 * [initialExtras] will be copied into the resulting [MutableCreationExtras] first.
 * Then the [builderAction] will be applied to the [MutableCreationExtras].
 *
 * @param initialExtras extras that will be filled into the resulting [MutableCreationExtras] first.
 * @param builderAction a lambda which will be applied to the [MutableCreationExtras].
 */
public inline fun buildCreationExtras(
  initialExtras: CreationExtras = EmptyCreationExtras,
  builderAction: MutableCreationExtras.() -> Unit,
): CreationExtras = MutableCreationExtras(initialExtras).apply(builderAction)

/**
 * Edits this [CreationExtras] by populating a [MutableCreationExtras] using the given [builderAction].
 * Content of this [CreationExtras] will be copied into the resulting [MutableCreationExtras] first.
 * Then the [builderAction] will be applied to the [MutableCreationExtras].
 *
 * @see buildCreationExtras
 */
public inline fun CreationExtras.edit(
  builderAction: MutableCreationExtras.() -> Unit,
): CreationExtras = buildCreationExtras(this, builderAction)
