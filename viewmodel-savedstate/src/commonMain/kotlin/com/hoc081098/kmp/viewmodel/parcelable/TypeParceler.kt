package com.hoc081098.kmp.viewmodel.parcelable

/**
 * A multiplatform alias for `kotlinx.parcelize.TypeParceler` from the `kotlin-parcelize` Gradle plugin.
 * Make sure you have the `kotlin-parcelize` Gradle plugin applied.
 *
 * Specifies what [Parceler] should be used for a particular type [T].
 */
@Retention(AnnotationRetention.SOURCE)
@Repeatable
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
public expect annotation class TypeParceler<T, P : Parceler<in T>>()
