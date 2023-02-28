package com.hoc081098.kmp.viewmodel.parcelable

/**
 * A multiplatform alias for `kotlinx.parcelize.IgnoredOnParcel` from the `kotlin-parcelize` Gradle plugin.
 * Make sure you have the `kotlin-parcelize` Gradle plugin applied.
 *
 * The property annotated with [IgnoredOnParcel] will not be stored into parcel.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
public expect annotation class IgnoredOnParcel()
