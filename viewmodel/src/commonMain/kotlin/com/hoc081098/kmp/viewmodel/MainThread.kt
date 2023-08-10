package com.hoc081098.kmp.viewmodel

/**
 * Denotes that the annotated method should only be called on the main thread.
 * If the annotated element is a class, then all methods in the class should be called
 * on the main thread.
 */
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@Target(
  AnnotationTarget.FUNCTION,
  AnnotationTarget.CONSTRUCTOR,
  AnnotationTarget.TYPE,
  AnnotationTarget.TYPE_PARAMETER,
)
public expect annotation class MainThread()
