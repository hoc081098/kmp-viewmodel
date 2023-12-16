package com.hoc081098.kmp.viewmodel

import com.hoc081098.kmp.viewmodel.safe.NullableSavedStateHandleKey
import com.hoc081098.kmp.viewmodel.safe.boolean
import com.hoc081098.kmp.viewmodel.safe.booleanArray
import com.hoc081098.kmp.viewmodel.safe.byte
import com.hoc081098.kmp.viewmodel.safe.byteArray
import com.hoc081098.kmp.viewmodel.safe.char
import com.hoc081098.kmp.viewmodel.safe.charArray
import com.hoc081098.kmp.viewmodel.safe.charSequence
import com.hoc081098.kmp.viewmodel.safe.charSequenceArray
import com.hoc081098.kmp.viewmodel.safe.charSequenceArrayList
import com.hoc081098.kmp.viewmodel.safe.double
import com.hoc081098.kmp.viewmodel.safe.doubleArray
import com.hoc081098.kmp.viewmodel.safe.float
import com.hoc081098.kmp.viewmodel.safe.floatArray
import com.hoc081098.kmp.viewmodel.safe.int
import com.hoc081098.kmp.viewmodel.safe.intArray
import com.hoc081098.kmp.viewmodel.safe.intArrayList
import com.hoc081098.kmp.viewmodel.safe.long
import com.hoc081098.kmp.viewmodel.safe.longArray
import com.hoc081098.kmp.viewmodel.safe.parcelable
import com.hoc081098.kmp.viewmodel.safe.parcelableArray
import com.hoc081098.kmp.viewmodel.safe.parcelableArrayList
import com.hoc081098.kmp.viewmodel.safe.safe
import com.hoc081098.kmp.viewmodel.safe.short
import com.hoc081098.kmp.viewmodel.safe.shortArray
import com.hoc081098.kmp.viewmodel.safe.string
import com.hoc081098.kmp.viewmodel.safe.stringArray
import com.hoc081098.kmp.viewmodel.safe.stringArrayList
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest

val nullableKeyAndNextValues: List<Pair<NullableSavedStateHandleKey<out Any>, Any?>> = listOf(
  NullableSavedStateHandleKey.boolean("boolean", false) to
    true,
  NullableSavedStateHandleKey.booleanArray("booleanArray", booleanArrayOf(false)) to
    booleanArrayOf(true),
  NullableSavedStateHandleKey.double("double", 0.0) to 1.0,
  NullableSavedStateHandleKey.doubleArray("doubleArray", doubleArrayOf(0.0)) to
    doubleArrayOf(1.0),
  NullableSavedStateHandleKey.int("int", 0) to
    1,
  NullableSavedStateHandleKey.intArray("intArray", intArrayOf(0)) to
    intArrayOf(1),
  NullableSavedStateHandleKey.long("long", 0L) to
    1L,
  NullableSavedStateHandleKey.longArray("longArray", longArrayOf(0L)) to
    longArrayOf(1L),
  NullableSavedStateHandleKey.string("string", "") to
    "hoc081098",
  NullableSavedStateHandleKey.stringArray("stringArray", arrayOf("")) to
    arrayOf("hoc081098"),
  NullableSavedStateHandleKey.byte("byte", 0) to
    1.toByte(),
  NullableSavedStateHandleKey.byteArray("byteArray", byteArrayOf(0)) to
    byteArrayOf(1),
  NullableSavedStateHandleKey.char("char", 'A') to
    'B',
  NullableSavedStateHandleKey.charArray("charArray", charArrayOf('A')) to
    charArrayOf('B'),
  NullableSavedStateHandleKey.charSequence("charSequence", StringBuilder("")) to
    StringBuilder("hoc081098"),
  NullableSavedStateHandleKey.charSequenceArray("charSequenceArray", arrayOf(StringBuilder(""))) to
    arrayOf(StringBuilder("hoc081098")),
  NullableSavedStateHandleKey.float("float", 0f) to
    1f,
  NullableSavedStateHandleKey.floatArray("floatArray", floatArrayOf(0f)) to
    floatArrayOf(1f),
  NullableSavedStateHandleKey.parcelable("parcelable", TestParcelable(0)) to
    TestParcelable(1),
  NullableSavedStateHandleKey.parcelableArray("parcelableArray", arrayOf(TestParcelable(0))) to
    TestParcelable(1),
  NullableSavedStateHandleKey.short("short", 0) to
    1.toShort(),
  NullableSavedStateHandleKey.shortArray("shortArray", shortArrayOf(0)) to
    shortArrayOf(1),
  NullableSavedStateHandleKey.parcelableArrayList("parcelableArrayList", arrayListOf(TestParcelable(0))) to
    arrayListOf(TestParcelable(1)),
  NullableSavedStateHandleKey.intArrayList("intArrayList", arrayListOf(0)) to
    arrayListOf(1),
  NullableSavedStateHandleKey.stringArrayList("stringArrayList", arrayListOf("")) to
    arrayListOf("hoc081098"),
  NullableSavedStateHandleKey.charSequenceArrayList("charSequenceArrayList", arrayListOf(StringBuilder(""))) to
    arrayListOf(StringBuilder("hoc081098")),
)

class NullableSavedStateHandleKeyTest {
  @Test
  fun nullAssociated() {
    val savedStateHandle = SavedStateHandle()

    nullableKeyAndNextValues.forEach { (key, nextValue) ->
      savedStateHandle[key.key] = null
      assertNull(savedStateHandle.safe { it[key] })
    }
  }

  @Test
  fun nonNullAssociated() {
    val savedStateHandle = SavedStateHandle()

    nullableKeyAndNextValues.forEach { (key, nextValue) ->
      savedStateHandle[key.key] = nextValue
      assertEquals(nextValue, savedStateHandle[key.key])
    }
  }

  @Test
  fun read_then_writeNonNull_then_read() {
    val savedStateHandle = SavedStateHandle()

    nullableKeyAndNextValues.forEach { (key, nextValue) ->
      savedStateHandle.safe { safeSavedStateHandle ->
        // Read
        assertNull(savedStateHandle[key.key])
        assertFalse { key.key in savedStateHandle }

        assertEquals(key.defaultValue, safeSavedStateHandle[key])
        assertNull(savedStateHandle[key.key])

        // Update
        safeSavedStateHandle[key as NullableSavedStateHandleKey<Any>] = nextValue

        // Read
        assertTrue { key.key in savedStateHandle }

        assertEquals(nextValue, safeSavedStateHandle[key])
        assertEquals(nextValue, savedStateHandle[key.key])
      }
    }
  }

  @Test
  fun read_then_writeNull_then_read() {
    val savedStateHandle = SavedStateHandle()

    nullableKeyAndNextValues.forEach { (key) ->
      savedStateHandle.safe { safeSavedStateHandle ->
        // Read
        assertNull(savedStateHandle[key.key])
        assertFalse { key.key in savedStateHandle }

        assertEquals(key.defaultValue, safeSavedStateHandle[key])
        assertNull(savedStateHandle[key.key])

        // Update
        safeSavedStateHandle[key as NullableSavedStateHandleKey<Any>] = null

        // Read
        assertTrue { key.key in savedStateHandle }

        assertNull(safeSavedStateHandle[key])
        assertNull(savedStateHandle[key.key])
      }
    }
  }

  @Test
  fun read_then_remove_then_read() {
    val savedStateHandle = SavedStateHandle()

    nullableKeyAndNextValues.forEach { (key, nextValue) ->
      savedStateHandle.safe { safeSavedStateHandle ->
        // Read
        assertNull(savedStateHandle[key.key])
        assertFalse { key.key in savedStateHandle }

        assertEquals(key.defaultValue, safeSavedStateHandle[key])
        assertNull(savedStateHandle[key.key])

        // Update
        safeSavedStateHandle.remove(key)

        // Read
        assertFalse { key.key in savedStateHandle }

        assertEquals(key.defaultValue, safeSavedStateHandle[key])
        assertNull(savedStateHandle[key.key])
      }
    }
  }

  @Test
  fun getStateFlow() = runTest(UnconfinedTestDispatcher()) {
    val savedStateHandle = SavedStateHandle()

    nullableKeyAndNextValues.forEach { (key, nextValue) ->
      val stateFlow = savedStateHandle.safe { it.getStateFlow(key) }
      assertEquals(key.defaultValue, stateFlow.value)

      val deferred = async { stateFlow.take(3).toList() }

      savedStateHandle.safe { it[key as NullableSavedStateHandleKey<Any>] = null }
      savedStateHandle.safe { it[key as NullableSavedStateHandleKey<Any>] = nextValue }

      assertContentEquals(
        expected = listOf(key.defaultValue, null, nextValue),
        actual = deferred.await(),
      )
    }
  }
}
