package com.hoc081098.kmp.viewmodel.parcelable

@Retention(AnnotationRetention.SOURCE)
@Repeatable
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
public actual annotation class TypeParceler<T, P : Parceler<in T>> actual constructor()
