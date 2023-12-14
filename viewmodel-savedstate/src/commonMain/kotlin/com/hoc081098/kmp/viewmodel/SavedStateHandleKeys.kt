package com.hoc081098.kmp.viewmodel

import com.hoc081098.kmp.viewmodel.parcelable.Parcelable
import kotlin.jvm.JvmStatic

public fun SavedStateHandleKey.Companion.boolean(key: String, defaultValue: Boolean): SavedStateHandleKey<Boolean> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.booleanArray(
  key: String,
  defaultValue: BooleanArray,
): SavedStateHandleKey<BooleanArray> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.double(
  key: String,
  defaultValue: Double,
): SavedStateHandleKey<Double> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.doubleArray(
  key: String,
  defaultValue: DoubleArray,
): SavedStateHandleKey<DoubleArray> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.int(
  key: String,
  defaultValue: Int,
): SavedStateHandleKey<Int> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.intArray(
  key: String,
  defaultValue: IntArray,
): SavedStateHandleKey<IntArray> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.long(
  key: String,
  defaultValue: Long,
): SavedStateHandleKey<Long> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.longArray(
  key: String,
  defaultValue: LongArray,
): SavedStateHandleKey<LongArray> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.string(
  key: String,
  defaultValue: String,
): SavedStateHandleKey<String> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.stringArray(
  key: String,
  defaultValue: Array<String>,
): SavedStateHandleKey<Array<String>> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.byte(
  key: String,
  defaultValue: Byte,
): SavedStateHandleKey<Byte> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.byteArray(
  key: String,
  defaultValue: ByteArray,
): SavedStateHandleKey<ByteArray> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.char(
  key: String,
  defaultValue: Char,
): SavedStateHandleKey<Char> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.charArray(
  key: String,
  defaultValue: CharArray,
): SavedStateHandleKey<CharArray> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.charSequence(
  key: String,
  defaultValue: CharSequence,
): SavedStateHandleKey<CharSequence> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.charSequenceArray(
  key: String,
  defaultValue: Array<CharSequence>,
): SavedStateHandleKey<Array<CharSequence>> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.float(
  key: String,
  defaultValue: Float,
): SavedStateHandleKey<Float> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.floatArray(
  key: String,
  defaultValue: FloatArray,
): SavedStateHandleKey<FloatArray> =
  SavedStateHandleKey(key, defaultValue)

public fun <T : Parcelable> SavedStateHandleKey.Companion.parcelable(
  key: String,
  defaultValue: T,
): SavedStateHandleKey<T> =
  SavedStateHandleKey(key, defaultValue)

public fun <T : Parcelable> SavedStateHandleKey.Companion.parcelableArray(
  key: String,
  defaultValue: Array<T>,
): SavedStateHandleKey<Array<T>> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.short(
  key: String,
  defaultValue: Short,
): SavedStateHandleKey<Short> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.shortArray(
  key: String,
  defaultValue: ShortArray,
): SavedStateHandleKey<ShortArray> =
  SavedStateHandleKey(key, defaultValue)

public fun <T : Parcelable> SavedStateHandleKey.Companion.parcelableArrayList(
  key: String,
  defaultValue: ArrayList<T>,
): SavedStateHandleKey<ArrayList<T>> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.intArrayList(
  key: String,
  defaultValue: ArrayList<Int>,
): SavedStateHandleKey<ArrayList<Int>> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.stringArrayList(
  key: String,
  defaultValue: ArrayList<String>,
): SavedStateHandleKey<ArrayList<String>> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.charSequenceArrayList(
  key: String,
  defaultValue: ArrayList<CharSequence>,
): SavedStateHandleKey<ArrayList<CharSequence>> =
  SavedStateHandleKey(key, defaultValue)
