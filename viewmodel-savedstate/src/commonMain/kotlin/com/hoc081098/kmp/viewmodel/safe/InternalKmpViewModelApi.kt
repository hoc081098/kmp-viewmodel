package com.hoc081098.kmp.viewmodel.safe

/**
 * Marks declarations in the `com.hoc081098.kmp.viewmodel.safe` that are **delicate** &mdash;
 * they have limited use-case and shall be used with care in general code.
 *
 * Carefully read documentation of any declaration marked as `DelicateSafeSavedStateHandleApi`.
 */
@MustBeDocumented
@Retention(value = AnnotationRetention.BINARY)
@Target(
  AnnotationTarget.ANNOTATION_CLASS,
  AnnotationTarget.PROPERTY,
  AnnotationTarget.CONSTRUCTOR,
  AnnotationTarget.FUNCTION,
)
@RequiresOptIn(
  level = RequiresOptIn.Level.WARNING,
  message = "This is a delicate API and its use requires care." +
    " Make sure you fully read and understand documentation of the declaration that is marked as a delicate API.",
)
public annotation class DelicateSafeSavedStateHandleApi
