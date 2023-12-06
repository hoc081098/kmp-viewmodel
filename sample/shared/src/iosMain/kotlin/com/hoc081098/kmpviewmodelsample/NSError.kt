@file:Suppress("unused") // Called from platform code
@file:OptIn(ExperimentalForeignApi::class)

package com.hoc081098.kmpviewmodelsample

import io.github.aakira.napier.Napier
import kotlin.native.internal.ObjCErrorException
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.convert
import platform.Foundation.NSError
import platform.Foundation.NSLocalizedDescriptionKey
import platform.darwin.NSInteger

/**
 * Converts a [Throwable] to a [NSError].
 *
 * The returned [NSError] has `KotlinException` as the [NSError.domain], `0` as the [NSError.code] and
 * the [NSError.localizedDescription] is set to the [Throwable.message].
 *
 * The Kotlin throwable can be retrieved from the [NSError.userInfo] with the key `KotlinException`.
 */
@OptIn(ExperimentalForeignApi::class)
fun Throwable.asNSError(): NSError {
  val userInfo = buildMap<Any?, Any> {
    this["KotlinException"] = this@asNSError
    message?.let {
      this[NSLocalizedDescriptionKey] = it
    }
    this["KotlinExceptionOrigin"] = ""
  }
  return NSError.errorWithDomain(
    domain = "KotlinException",
    code = 0.convert(),
    userInfo = userInfo,
  )
}

/**
 * Indicates if `this` [NSError] represents a [Throwable].
 */
val NSError.isKotlinThrowable: Boolean
  get() {
    return domain == "KotlinException" &&
      code == 0.convert<NSInteger>() &&
      userInfo["KotlinException"] is Throwable &&
      userInfo["KotlinExceptionOrigin"] != null
  }

/**
 * Converts `this` [NSError] to a [Throwable].
 *
 * If `this` [NSError] represents a [Throwable], the original [Throwable] is returned.
 * For other [NSError]s an [ObjCErrorException] will be returned.
 *
 * @see asNSError
 */
fun NSError.asThrowable(): Throwable {
  return if (isKotlinThrowable) {
    Napier.d(message = "isKotlinThrowable this=$this $userInfo", tag = "NSError.asThrowable")

    userInfo["KotlinException"] as Throwable
  } else {
    Napier.d(message = "ObjCErrorException this=$this", tag = "NSError.asThrowable")

    ObjCErrorException(
      message = localizedDescription,
      error = this,
    )
  }
}
