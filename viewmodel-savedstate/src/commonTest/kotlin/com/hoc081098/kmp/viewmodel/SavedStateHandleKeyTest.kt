package com.hoc081098.kmp.viewmodel

import com.hoc081098.kmp.viewmodel.parcelable.Parcelable
import com.hoc081098.kmp.viewmodel.parcelable.Parcelize
import com.hoc081098.kmp.viewmodel.safe.SavedStateHandleKey
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
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

val nonNullKeyAndNextValues = listOf(
  SavedStateHandleKey.boolean("boolean", false) to
    true,
  SavedStateHandleKey.booleanArray("booleanArray", booleanArrayOf(false)) to
    booleanArrayOf(true),
  SavedStateHandleKey.double("double", 0.0) to 1.0,
  SavedStateHandleKey.doubleArray("doubleArray", doubleArrayOf(0.0)) to
    doubleArrayOf(1.0),
  SavedStateHandleKey.int("int", 0) to
    1,
  SavedStateHandleKey.intArray("intArray", intArrayOf(0)) to
    intArrayOf(1),
  SavedStateHandleKey.long("long", 0L) to
    1L,
  SavedStateHandleKey.longArray("longArray", longArrayOf(0L)) to
    longArrayOf(1L),
  SavedStateHandleKey.string("string", "") to
    "hoc081098",
  SavedStateHandleKey.stringArray("stringArray", arrayOf("")) to
    arrayOf("hoc081098"),
  SavedStateHandleKey.byte("byte", 0) to
    1.toByte(),
  SavedStateHandleKey.byteArray("byteArray", byteArrayOf(0)) to
    byteArrayOf(1),
  SavedStateHandleKey.char("char", 'A') to
    'B',
  SavedStateHandleKey.charArray("charArray", charArrayOf('A')) to
    charArrayOf('B'),
  SavedStateHandleKey.charSequence("charSequence", StringBuilder("")) to
    StringBuilder("hoc081098"),
  SavedStateHandleKey.charSequenceArray("charSequenceArray", arrayOf(StringBuilder(""))) to
    arrayOf(StringBuilder("hoc081098")),
  SavedStateHandleKey.float("float", 0f) to
    1f,
  SavedStateHandleKey.floatArray("floatArray", floatArrayOf(0f)) to
    floatArrayOf(1f),
  SavedStateHandleKey.parcelable("parcelable", TestParcelable(0)) to
    TestParcelable(1),
  SavedStateHandleKey.parcelableArray("parcelableArray", arrayOf(TestParcelable(0))) to
    TestParcelable(1),
  SavedStateHandleKey.short("short", 0) to
    1.toShort(),
  SavedStateHandleKey.shortArray("shortArray", shortArrayOf(0)) to
    shortArrayOf(1),
  SavedStateHandleKey.parcelableArrayList("parcelableArrayList", arrayListOf(TestParcelable(0))) to
    arrayListOf(TestParcelable(1)),
  SavedStateHandleKey.intArrayList("intArrayList", arrayListOf(0)) to
    arrayListOf(1),
  SavedStateHandleKey.stringArrayList("stringArrayList", arrayListOf("")) to
    arrayListOf("hoc081098"),
  SavedStateHandleKey.charSequenceArrayList("charSequenceArrayList", arrayListOf(StringBuilder(""))) to
    arrayListOf(StringBuilder("hoc081098")),
)

@Parcelize
data class TestParcelable(val value: Int) : Parcelable

class SavedStateHandleKeyTest {
  @Test
  fun test() {
    val savedStateHandle = SavedStateHandle()

    nonNullKeyAndNextValues.forEach { (key, nextValue) ->
      savedStateHandle.safe { safeSavedStateHandle ->
        // Read
        assertNull(savedStateHandle[key.key])
        assertFalse { key.key in savedStateHandle }

        assertEquals(key.defaultValue, safeSavedStateHandle[key])
        assertEquals(key.defaultValue, savedStateHandle[key.key])

        assertNotNull(savedStateHandle[key.key])
        assertTrue { key.key in savedStateHandle }

        // Update
        safeSavedStateHandle[key as SavedStateHandleKey<Any>] = nextValue

        // Read
        assertTrue { key.key in savedStateHandle }
        assertEquals(nextValue, safeSavedStateHandle[key])
        assertEquals(nextValue, savedStateHandle[key.key])
      }
    }
  }
}
