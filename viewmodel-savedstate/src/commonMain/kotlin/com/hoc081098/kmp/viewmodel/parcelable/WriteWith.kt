package com.hoc081098.kmp.viewmodel.parcelable

/**
 * A multiplatform alias for `kotlinx.parcelize.WriteWith` from the `kotlin-parcelize` Gradle plugin.
 * Make sure you have the `kotlin-parcelize` Gradle plugin applied.
 *
 * Specifies what [Parceler] should be used for the annotated type.
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.TYPE)
public expect annotation class WriteWith<P : Parceler<*>>()
