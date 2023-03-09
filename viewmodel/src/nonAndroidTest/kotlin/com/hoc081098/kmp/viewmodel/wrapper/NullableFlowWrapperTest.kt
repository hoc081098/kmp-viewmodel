package com.hoc081098.kmp.viewmodel.wrapper

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

class NullableFlowWrapperTest {
  @Test
  fun both_normal() = runTest {
    val values = mutableListOf<Int?>()
    var error = null as Throwable?
    var completed = false

    NullableFlowWrapper(flowOf(1, 2, 3, null))
      .subscribe(
        scope = this,
        onValue = { values += it },
        onError = { error = it },
        onComplete = { completed = true },
      )
      .join()

    assertContentEquals(expected = listOf(1, 2, 3, null), actual = values)
    assertNull(error)
    assertTrue(completed)
  }

  @Test
  fun both_error() = runTest {
    val values = mutableListOf<Int?>()
    var error = null as Throwable?
    var completed = false

    NullableFlowWrapper(
      flow<Int?> {
        emit(1)
        emit(2)
        emit(null)
        throw RuntimeException()
      },
    )
      .subscribe(
        scope = this,
        onValue = { values += it },
        onError = { error = it },
        onComplete = { completed = true },
      )
      .join()

    assertContentEquals(expected = listOf(1, 2, null), actual = values)
    assertIs<RuntimeException>(error)
    assertFalse(completed)
  }

  @Test
  fun onErrorOnly_normal() = runTest {
    val values = mutableListOf<Int?>()
    var error = null as Throwable?

    NullableFlowWrapper(flowOf(1, 2, 3, null))
      .subscribe(
        scope = this,
        onValue = { values += it },
        onError = { error = it },
      )
      .join()

    assertContentEquals(expected = listOf(1, 2, 3, null), actual = values)
    assertNull(error)
  }

  @Test
  fun onErrorOnly_error() = runTest {
    val values = mutableListOf<Int?>()
    var error = null as Throwable?

    NullableFlowWrapper(
      flow<Int?> {
        emit(1)
        emit(2)
        emit(null)
        throw RuntimeException()
      },
    )
      .subscribe(
        scope = this,
        onValue = { values += it },
        onError = { error = it },
      )
      .join()

    assertContentEquals(expected = listOf(1, 2, null), actual = values)
    assertIs<RuntimeException>(error)
  }

  @Test
  fun onCompleteOnly_normal() = runTest {
    val values = mutableListOf<Int?>()
    var completed = false

    NullableFlowWrapper(flowOf(1, 2, 3, null))
      .subscribe(
        scope = this,
        onValue = { values += it },
        onComplete = { completed = true },
      )
      .join()

    assertContentEquals(expected = listOf(1, 2, 3, null), actual = values)
    assertTrue(completed)
  }

  @Test
  fun onCompleteOnly_error() = runTest {
    var error = null as Throwable?
    val values = mutableListOf<Int?>()
    var completed = false

    NullableFlowWrapper(
      flow<Int?> {
        emit(1)
        emit(2)
        emit(null)
        throw RuntimeException()
      },
    )
      .subscribe(
        scope = CoroutineScope(CoroutineExceptionHandler { _, throwable -> error = throwable }),
        onValue = { values += it },
        onComplete = { completed = true },
      )
      .join()

    assertIs<RuntimeException>(error)
    assertContentEquals(expected = listOf(1, 2, null), actual = values)
    assertFalse(completed)
  }
}
