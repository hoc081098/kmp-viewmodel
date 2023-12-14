package com.hoc081098.kmp.viewmodel

import com.hoc081098.kmp.viewmodel.parcelable.Parcelable
import kotlin.jvm.JvmName

@JvmName("booleanKey")
public fun booleanSavedStateHandleKey(key: String, defaultValue: Boolean): SavedStateHandleKey<Boolean> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("booleanArrayKey")
public fun booleanArraySavedStateHandleKey(
  key: String,
  defaultValue: BooleanArray
): SavedStateHandleKey<BooleanArray> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("doubleKey")
public fun doubleSavedStateHandleKey(key: String, defaultValue: Double): SavedStateHandleKey<Double> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("doubleArrayKey")
public fun doubleArraySavedStateHandleKey(key: String, defaultValue: DoubleArray): SavedStateHandleKey<DoubleArray> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("intKey")
public fun intSavedStateHandleKey(key: String, defaultValue: Int): SavedStateHandleKey<Int> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("intArrayKey")
public fun intArraySavedStateHandleKey(key: String, defaultValue: IntArray): SavedStateHandleKey<IntArray> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("longKey")
public fun longSavedStateHandleKey(key: String, defaultValue: Long): SavedStateHandleKey<Long> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("longArrayKey")
public fun longArraySavedStateHandleKey(key: String, defaultValue: LongArray): SavedStateHandleKey<LongArray> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("stringKey")
public fun stringSavedStateHandleKey(key: String, defaultValue: String): SavedStateHandleKey<String> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("stringArrayKey")
public fun stringArraySavedStateHandleKey(
  key: String,
  defaultValue: Array<String>
): SavedStateHandleKey<Array<String>> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("byteKey")
public fun byteSavedStateHandleKey(key: String, defaultValue: Byte): SavedStateHandleKey<Byte> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("byteArrayKey")
public fun byteArraySavedStateHandleKey(key: String, defaultValue: ByteArray): SavedStateHandleKey<ByteArray> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("charKey")
public fun charSavedStateHandleKey(key: String, defaultValue: Char): SavedStateHandleKey<Char> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("charArrayKey")
public fun charArraySavedStateHandleKey(key: String, defaultValue: CharArray): SavedStateHandleKey<CharArray> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("charSequenceKey")
public fun charSequenceSavedStateHandleKey(
  key: String,
  defaultValue: CharSequence
): SavedStateHandleKey<CharSequence> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("charSequenceArrayKey")
public fun charSequenceArraySavedStateHandleKey(
  key: String,
  defaultValue: Array<CharSequence>
): SavedStateHandleKey<Array<CharSequence>> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("floatKey")
public fun floatSavedStateHandleKey(key: String, defaultValue: Float): SavedStateHandleKey<Float> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("floatArrayKey")
public fun floatArraySavedStateHandleKey(key: String, defaultValue: FloatArray): SavedStateHandleKey<FloatArray> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("parcelableKey")
public fun <T : Parcelable> parcelableSavedStateHandleKey(key: String, defaultValue: T): SavedStateHandleKey<T> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("parcelableArrayKey")
public fun <T : Parcelable> parcelableArraySavedStateHandleKey(
  key: String,
  defaultValue: Array<T>
): SavedStateHandleKey<Array<T>> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("shortKey")
public fun shortSavedStateHandleKey(key: String, defaultValue: Short): SavedStateHandleKey<Short> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("shortArrayKey")
public fun shortArraySavedStateHandleKey(key: String, defaultValue: ShortArray): SavedStateHandleKey<ShortArray> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("parcelableArrayListKey")
public fun <T : Parcelable> parcelableArrayListSavedStateHandleKey(
  key: String,
  defaultValue: ArrayList<T>
): SavedStateHandleKey<ArrayList<T>> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("intArrayListKey")
public fun intArrayListSavedStateHandleKey(
  key: String,
  defaultValue: ArrayList<Int>
): SavedStateHandleKey<ArrayList<Int>> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("stringArrayListKey")
public fun stringArrayListSavedStateHandleKey(
  key: String,
  defaultValue: ArrayList<String>
): SavedStateHandleKey<ArrayList<String>> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("charSequenceArrayListKey")
public fun charSequenceArrayListSavedStateHandleKey(
  key: String,
  defaultValue: ArrayList<CharSequence>
): SavedStateHandleKey<ArrayList<CharSequence>> =
  SavedStateHandleKey(key, defaultValue)
