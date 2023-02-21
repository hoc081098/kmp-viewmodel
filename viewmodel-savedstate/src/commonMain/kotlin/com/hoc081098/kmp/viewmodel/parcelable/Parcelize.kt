package com.hoc081098.kmp.viewmodel.parcelable

/**
 * A multiplatform alias for `kotlinx.parcelize.Parcelize` from the `kotlin-parcelize` Gradle plugin.
 * Make sure you have the `kotlin-parcelize` Gradle plugin applied.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
public expect annotation class Parcelize()
