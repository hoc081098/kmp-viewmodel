package com.hoc081098.kmp.viewmodel.safe

import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.TestParcelable
import com.hoc081098.kmp.viewmodel.TestSerializable
import com.hoc081098.kmp.viewmodel.extendedAssertEquals
import kotlin.test.Test
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
  NonNullSavedStateHandleKey.serializable("serializable", TestSerializable(0)) to
    TestSerializable(1),
  NonNullSavedStateHandleKey.parcelable("parcelable", TestParcelable(0)) to
    TestParcelable(1),
  NonNullSavedStateHandleKey.parcelableArray("parcelableArray", arrayOf(TestParcelable(0), null)) to
    arrayOf(TestParcelable(1), null),
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

@OptIn(DelicateSafeSavedStateHandleApi::class)
class NonNullSavedStateHandleKeyTest {
  @Test
  fun nullAssociated() {
    val savedStateHandle = SavedStateHandle()

    nonNullKeyAndNextValues.forEach { (key) ->
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
      extendedAssertEquals(nextValue, savedStateHandle.safe { it[key] })
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

        extendedAssertEquals(key.defaultValue, safeSavedStateHandle[key])
        assertNull(savedStateHandle[key.key])

        // Update
        @Suppress("UNCHECKED_CAST")
        safeSavedStateHandle[key as NonNullSavedStateHandleKey<Any>] = nextValue

        // Read
        assertTrue { key.key in savedStateHandle }

        extendedAssertEquals(nextValue, safeSavedStateHandle[key])
        extendedAssertEquals(nextValue, savedStateHandle[key.key])
      }
    }
  }

  @Test
  fun getStateFlow_noExistingValue() = runTest(UnconfinedTestDispatcher()) {
    val savedStateHandle = SavedStateHandle()

    nonNullKeyAndNextValues.forEach { (key, nextValue) ->
      val stateFlow = savedStateHandle.safe { it.getStateFlow(key) }
      extendedAssertEquals(key.defaultValue, stateFlow.value)

      val deferred = async { stateFlow.take(2).toList() }

      @Suppress("UNCHECKED_CAST")
      savedStateHandle.safe { it[key as NonNullSavedStateHandleKey<Any>] = nextValue }

      extendedAssertEquals(
        expected = listOf(key.defaultValue, nextValue),
        actual = deferred.await(),
      )
    }
  }

  @Test
  fun getStateFlow_existingValue() = runTest(UnconfinedTestDispatcher()) {
    val savedStateHandle = SavedStateHandle()

    nonNullKeyAndNextValues.forEach { (key, nextValue) ->
      savedStateHandle[key.key] = nextValue

      val stateFlow = savedStateHandle.safe { it.getStateFlow(key) }
      extendedAssertEquals(nextValue, stateFlow.value)

      val deferred = async { stateFlow.take(2).toList() }

      @Suppress("UNCHECKED_CAST")
      savedStateHandle.safe { it[key as NonNullSavedStateHandleKey<Any>] = key.defaultValue }

      extendedAssertEquals(
        expected = listOf(nextValue, key.defaultValue),
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
