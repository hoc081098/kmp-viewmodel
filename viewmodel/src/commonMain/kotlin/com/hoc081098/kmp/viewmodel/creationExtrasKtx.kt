package com.hoc081098.kmp.viewmodel

/**
 * Builds a [CreationExtras] by populating a [MutableCreationExtrasBuilder] using the given [builderAction].
 * [initialExtras] will be copied into the resulting [MutableCreationExtrasBuilder] first.
 * Then the [builderAction] will be applied to the [MutableCreationExtrasBuilder].
 *
 * @param initialExtras extras that will be filled into the resulting [MutableCreationExtrasBuilder] first.
 * @param builderAction a lambda which will be applied to the [MutableCreationExtrasBuilder].
 */
public inline fun buildCreationExtras(
  initialExtras: CreationExtras = EmptyCreationExtras,
  builderAction: MutableCreationExtrasBuilder.() -> Unit,
): CreationExtras = MutableCreationExtrasBuilder(initialExtras)
  .apply(builderAction)
  .asCreationExtras()

/**
 * Edits this [CreationExtras] by populating a [MutableCreationExtrasBuilder] using the given [builderAction].
 * Content of this [CreationExtras] will be copied into the resulting [MutableCreationExtrasBuilder] first.
 * Then the [builderAction] will be applied to the [MutableCreationExtrasBuilder].
 *
 * @see buildCreationExtras
 */
public inline fun CreationExtras.edit(
  builderAction: MutableCreationExtrasBuilder.() -> Unit,
): CreationExtras = buildCreationExtras(this, builderAction)
