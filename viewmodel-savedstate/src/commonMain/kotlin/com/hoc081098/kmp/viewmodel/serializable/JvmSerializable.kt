package com.hoc081098.kmp.viewmodel.serializable

/**
 * Multiplatform reference to Java `java.io.Serializable` interface.
 *
 * Interface that indicates that the implementing object can be serialized using JVM specific serialization.
 * On non-JVM platforms, this interface is a no-op and does not provide any actual serialization capabilities.
 */
public expect interface JvmSerializable

@Deprecated(
  message = "JvmSerializable should be used instead to avoid confusion with KotlinX-Serialization @Serializable",
  replaceWith = ReplaceWith("com.hoc081098.kmp.viewmodel.serializable.JvmSerializable"),
  level = DeprecationLevel.WARNING,
)
public typealias Serializable = JvmSerializable
