package com.hoc081098.kmp.viewmodel

import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

fun <T> extendedAssertEquals(expected: T, actual: T, message: String? = null) = when (expected) {
  is Iterable<*> -> {
    check(actual is Iterable<*>) { "actual value is not an iterable" }

    fun Iterable<Any?>.transformArrayToList(): List<Any?> = map {
      if (it is Array<*>) {
        it.toList()
      } else {
        it
      }
    }

    assertContentEquals(
      expected = expected.transformArrayToList(),
      actual = actual.transformArrayToList(),
      message = message,
    )
  }

  is Array<*> -> {
    check(actual is Array<*>) { "actual value is not an array" }

    @Suppress("UNCHECKED_CAST")
    assertContentEquals(
      expected = expected as Array<Any?>,
      actual = actual as Array<Any?>,
      message = message,
    )
  }

  else -> {
    assertEquals(expected, actual, message)
  }
}
