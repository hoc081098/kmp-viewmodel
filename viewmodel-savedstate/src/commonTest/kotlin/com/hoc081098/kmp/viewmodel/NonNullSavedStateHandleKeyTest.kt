package com.hoc081098.kmp.viewmodel

import com.hoc081098.kmp.viewmodel.safe.NonNullSavedStateHandleKey
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
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest

val nonNullKeyAndNextValues: List<Pair<NonNullSavedStateHandleKey<out Any>, Any>> = listOf(
  NonNullSavedStateHandleKey.boolean("boolean", false) to
    true,
  NonNullSavedStateHandleKey.booleanArray("booleanArray", booleanArrayOf(false)) to
    booleanArrayOf(true),
  NonNullSavedStateHandleKey.double("double", 0.0) to 1.0,
  NonNullSavedStateHandleKey.doubleArray("doubleArray", doubleArrayOf(0.0)) to
    doubleArrayOf(1.0),
  NonNullSavedStateHandleKey.int("int", 0) to
    1,
  NonNullSavedStateHandleKey.intArray("intArray", intArrayOf(0)) to
    intArrayOf(1),
  NonNullSavedStateHandleKey.long("long", 0L) to
    1L,
  NonNullSavedStateHandleKey.longArray("longArray", longArrayOf(0L)) to
    longArrayOf(1L),
  NonNullSavedStateHandleKey.string("string", "") to
    "hoc081098",
  NonNullSavedStateHandleKey.stringArray("stringArray", arrayOf("")) to
    arrayOf("hoc081098"),
  NonNullSavedStateHandleKey.byte("byte", 0) to
    1.toByte(),
  NonNullSavedStateHandleKey.byteArray("byteArray", byteArrayOf(0)) to
    byteArrayOf(1),
  NonNullSavedStateHandleKey.char("char", 'A') to
    'B',
  NonNullSavedStateHandleKey.charArray("charArray", charArrayOf('A')) to
    charArrayOf('B'),
  NonNullSavedStateHandleKey.charSequence("charSequence", StringBuilder("")) to
    StringBuilder("hoc081098"),
  NonNullSavedStateHandleKey.charSequenceArray("charSequenceArray", arrayOf(StringBuilder(""), null)) to
    arrayOf(StringBuilder("hoc081098")),
  NonNullSavedStateHandleKey.float("float", 0f) to
    1f,
  NonNullSavedStateHandleKey.floatArray("floatArray", floatArrayOf(0f)) to
    floatArrayOf(1f),
  NonNullSavedStateHandleKey.parcelable("parcelable", TestParcelable(0)) to
    TestParcelable(1),
  NonNullSavedStateHandleKey.parcelableArray("parcelableArray", arrayOf(TestParcelable(0), null)) to
    TestParcelable(1),
  NonNullSavedStateHandleKey.short("short", 0) to
    1.toShort(),
  NonNullSavedStateHandleKey.shortArray("shortArray", shortArrayOf(0)) to
    shortArrayOf(1),
  NonNullSavedStateHandleKey.parcelableArrayList("parcelableArrayList", arrayListOf(TestParcelable(0), null)) to
    arrayListOf(TestParcelable(1)),
  NonNullSavedStateHandleKey.intArrayList("intArrayList", arrayListOf(0, null)) to
    arrayListOf(1),
  NonNullSavedStateHandleKey.stringArrayList("stringArrayList", arrayListOf("", null)) to
    arrayListOf("hoc081098"),
  NonNullSavedStateHandleKey.charSequenceArrayList("charSequenceArrayList", arrayListOf(StringBuilder(""), null)) to
    arrayListOf(StringBuilder("hoc081098")),
)

class NonNullSavedStateHandleKeyTest {
  @Test
  fun nullAssociated() {
    val savedStateHandle = SavedStateHandle()

    nonNullKeyAndNextValues.forEach { (key, nextValue) ->
      savedStateHandle[key.key] = null

      assertFailsWith<NullPointerException> {
        savedStateHandle.safe { it[key] }
      }
    }
  }

  @Test
  fun nonNullAssociated() {
    val savedStateHandle = SavedStateHandle()

    nonNullKeyAndNextValues.forEach { (key, nextValue) ->
      savedStateHandle[key.key] = nextValue
      assertEquals(nextValue, savedStateHandle.safe { it[key] })
    }
  }

  @Test
  fun read_then_write_then_read() {
    val savedStateHandle = SavedStateHandle()

    nonNullKeyAndNextValues.forEach { (key, nextValue) ->
      savedStateHandle.safe { safeSavedStateHandle ->
        // Read
        assertNull(savedStateHandle[key.key])
        assertFalse { key.key in savedStateHandle }

        assertEquals(key.defaultValue, safeSavedStateHandle[key])
        assertNull(savedStateHandle[key.key])

        // Update
        safeSavedStateHandle[key as NonNullSavedStateHandleKey<Any>] = nextValue

        // Read
        assertTrue { key.key in savedStateHandle }

        assertEquals(nextValue, safeSavedStateHandle[key])
        assertEquals(nextValue, savedStateHandle[key.key])
      }
    }
  }

  @Test
  fun getStateFlow() = runTest(UnconfinedTestDispatcher()) {
    val savedStateHandle = SavedStateHandle()

    nonNullKeyAndNextValues.forEach { (key, nextValue) ->
      val stateFlow = savedStateHandle.safe { it.getStateFlow(key) }
      assertEquals(key.defaultValue, stateFlow.value)

      val deferred = async { stateFlow.take(2).toList() }

      savedStateHandle.safe { it[key as NonNullSavedStateHandleKey<Any>] = nextValue }

      assertContentEquals(
        expected = listOf(key.defaultValue, nextValue),
        actual = deferred.await(),
      )
    }
  }

  @Test
  fun hashCodeAndEquals() {
    nonNullKeyAndNextValues.forEach {
      assertTrue { it.first == it.first }
      assertTrue { it.first.hashCode() == it.first.hashCode() }
    }

    assertFalse {
      NonNullSavedStateHandleKey.int("int", 0) ==
        NonNullSavedStateHandleKey.int("int", 1)
    }
    assertFalse {
      NonNullSavedStateHandleKey.int("int", 0).hashCode() ==
        NonNullSavedStateHandleKey.int("int", 1).hashCode()
    }

    assertFalse {
      NonNullSavedStateHandleKey.int("int", 0) ==
        NonNullSavedStateHandleKey.long("long", 1)
    }
    assertFalse {
      NonNullSavedStateHandleKey.int("int", 0).hashCode() ==
        NonNullSavedStateHandleKey.long("long", 1).hashCode()
    }
  }
}
