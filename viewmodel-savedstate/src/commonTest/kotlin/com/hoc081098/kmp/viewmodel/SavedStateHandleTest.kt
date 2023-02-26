@file:Suppress("RemoveExplicitTypeArguments")

package com.hoc081098.kmp.viewmodel

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.withIndex
import kotlinx.coroutines.test.runTest

// Most copied from https://github.com/androidx/androidx/blob/androidx-main/lifecycle/lifecycle-viewmodel-savedstate/src/androidTest/java/androidx/lifecycle/viewmodel/savedstate/SavedStateHandleTest.kt
class SavedStateHandleTest {
  @Test
  fun testSetGet() {
    val handle = SavedStateHandle()
    handle["foo"] = "trololo"
    assertEquals("trololo", handle.get<String?>("foo"))

    val fooSf = handle.getStateFlow<String?>("foo", null)
    assertEquals("trololo", fooSf.value)

    handle["foo"] = "another"
    assertEquals("another", handle.get<String?>("foo"))
  }

  @Test
  fun testSetNullGet() {
    val handle = SavedStateHandle()
    handle["foo"] = null
    assertEquals(null, handle.get<String?>("foo"))

    val fooSf = handle.getStateFlow<String?>("foo", null)
    assertEquals(null, fooSf.value)

    handle["foo"] = "another"
    assertEquals("another", handle.get<String?>("foo"))

    handle["foo"] = null
    assertEquals(null, handle.get<String?>("foo"))
  }

  @Test
  fun testSetObserve() = runTest {
    val handle = SavedStateHandle()

    val stateFlow = handle.getStateFlow<Int>("a", 0)
    val d = async {
      stateFlow
        .take(2)
        .toList()
    }

    delay(100)
    handle["a"] = 261
    delay(100)

    assertContentEquals(
      expected = listOf(0, 261),
      actual = d.await(),
    )
  }

  @Test
  fun testContains() {
    val handle = SavedStateHandle()

    assertFalse { handle.contains("foo") }

    handle["foo"] = 712
    assertTrue { handle.contains("foo") }

    handle.get<String>("foo2")
    assertFalse { handle.contains("foo2") }

    handle["foo2"] = "spb"
    assertTrue { handle.contains("foo2") }
  }

  @Test
  fun testRemove() {
    val handle = SavedStateHandle()

    handle["s"] = "pb"
    assertTrue { handle.contains("s") }
    assertEquals(
      expected = "pb",
      actual = handle.remove<String?>("s"),
    )
    assertFalse { handle.contains("s") }

    assertNull(handle.remove<String?>("don't exist"))
  }

  @Test
  fun testKeySet() {
    val accessor = SavedStateHandle()

    accessor["s"] = "pb"
    accessor.getStateFlow<String>("no value ld", "")

    assertEquals(2, accessor.keys().size)
    assertContentEquals(
      expected = listOf("s", "no value ld"),
      actual = accessor.keys().toList(),
    )
  }

  @Test
  fun savedStateValueFlow() = runTest {
    val handle = SavedStateHandle()

    handle
      .getStateFlow("test", 1)
      .take(3)
      .withIndex()
      .onEach { (index, value) ->
        val expectedValue = index + 1
        assertEquals(expectedValue, value)

        if (expectedValue < 3) {
          handle["test"] = expectedValue + 1
        }
      }
      .collect()
  }

  @Test
  fun newFlow_nullInitial() = runTest {
    val handle = SavedStateHandle()
    handle
      .getStateFlow<String?>("aa", null)
      .take(1)
      .onEach { assertNull(it) }
      .collect()
  }

  @Test
  fun newFlow_withInitialGet() = runTest {
    val handle = SavedStateHandle()
    val flow = handle.getStateFlow("aa", "xx")

    flow
      .take(1)
      .onEach {
        assertEquals(
          expected = "xx",
          actual = it,
        )
      }
      .collect()

    assertEquals(
      expected = "xx",
      actual = flow.value,
    )
  }

  @Test
  fun newFlow_existingValue_withInitial() = runTest {
    val handle = SavedStateHandle()
    handle["aa"] = "existing"

    handle.getStateFlow("aa", "xx")
      .take(1)
      .onEach {
        assertEquals(
          expected = "existing",
          actual = it,
        )
      }
      .collect()
  }

  @Test
  fun newFlow_existingValue_withNullInitial() = runTest {
    val handle = SavedStateHandle()
    handle["aa"] = "existing"

    handle.getStateFlow<String?>("aa", null)
      .take(1)
      .onEach {
        assertEquals(
          expected = "existing",
          actual = it,
        )
      }
      .collect()
  }

  @Test
  fun newFlow_existingNullValue_withInitial() = runTest {
    val handle = SavedStateHandle()
    handle["aa"] = null

    handle.getStateFlow<String?>("aa", "xx")
      .take(1)
      .onEach { assertNull(it) }
      .collect()
  }

  @Test
  fun newFlow_setNullValue_nonNullFlow() = runTest {
    val handle = SavedStateHandle()
    val flow = handle.getStateFlow("aa", "xx")

    flow.take(1)
      .onEach {
        assertEquals(
          expected = "xx",
          actual = it,
        )
      }
      .collect()

    handle["aa"] = null
    assertNull(flow.value)
  }
}
