package com.hoc081098.kmp.viewmodel

/**
 * Code marked with [InternalKmpViewModelApi] has no guarantees about API stability and can be changed
 * at any time.
 */
@MustBeDocumented
@Target(
  AnnotationTarget.ANNOTATION_CLASS,
  AnnotationTarget.PROPERTY,
  AnnotationTarget.CONSTRUCTOR,
  AnnotationTarget.FUNCTION,
)
@RequiresOptIn
@Retention(AnnotationRetention.BINARY)
public annotation class InternalKmpViewModelApi
