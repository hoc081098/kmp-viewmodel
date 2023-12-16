package com.hoc081098.kmp.viewmodel

import com.hoc081098.kmp.viewmodel.parcelable.Parcelable

private inline fun <T : Any> key(key: String, value: T) = SavedStateHandleKey(key, value)

public fun SavedStateHandleKey.Companion.boolean(key: String, defaultValue: Boolean): SavedStateHandleKey<Boolean> =
  key(key, defaultValue)

public fun SavedStateHandleKey.Companion.booleanArray(
  key: String,
  defaultValue: BooleanArray,
): SavedStateHandleKey<BooleanArray> =
  key(key, defaultValue)

public fun SavedStateHandleKey.Companion.double(
  key: String,
  defaultValue: Double,
): SavedStateHandleKey<Double> =
  key(key, defaultValue)

public fun SavedStateHandleKey.Companion.doubleArray(
  key: String,
  defaultValue: DoubleArray,
): SavedStateHandleKey<DoubleArray> =
  key(key, defaultValue)

public fun SavedStateHandleKey.Companion.int(
  key: String,
  defaultValue: Int,
): SavedStateHandleKey<Int> =
  key(key, defaultValue)

public fun SavedStateHandleKey.Companion.intArray(
  key: String,
  defaultValue: IntArray,
): SavedStateHandleKey<IntArray> =
  key(key, defaultValue)

public fun SavedStateHandleKey.Companion.long(
  key: String,
  defaultValue: Long,
): SavedStateHandleKey<Long> =
  key(key, defaultValue)

public fun SavedStateHandleKey.Companion.longArray(
  key: String,
  defaultValue: LongArray,
): SavedStateHandleKey<LongArray> =
  key(key, defaultValue)

public fun SavedStateHandleKey.Companion.string(
  key: String,
  defaultValue: String,
): SavedStateHandleKey<String> =
  key(key, defaultValue)

public fun SavedStateHandleKey.Companion.stringArray(
  key: String,
  defaultValue: Array<String>,
): SavedStateHandleKey<Array<String>> =
  key(key, defaultValue)

public fun SavedStateHandleKey.Companion.byte(
  key: String,
  defaultValue: Byte,
): SavedStateHandleKey<Byte> =
  key(key, defaultValue)

public fun SavedStateHandleKey.Companion.byteArray(
  key: String,
  defaultValue: ByteArray,
): SavedStateHandleKey<ByteArray> =
  key(key, defaultValue)

public fun SavedStateHandleKey.Companion.char(
  key: String,
  defaultValue: Char,
): SavedStateHandleKey<Char> =
  key(key, defaultValue)

public fun SavedStateHandleKey.Companion.charArray(
  key: String,
  defaultValue: CharArray,
): SavedStateHandleKey<CharArray> =
  key(key, defaultValue)

public fun SavedStateHandleKey.Companion.charSequence(
  key: String,
  defaultValue: CharSequence,
): SavedStateHandleKey<CharSequence> =
  key(key, defaultValue)

public fun SavedStateHandleKey.Companion.charSequenceArray(
  key: String,
  defaultValue: Array<CharSequence>,
): SavedStateHandleKey<Array<CharSequence>> =
  key(key, defaultValue)

public fun SavedStateHandleKey.Companion.float(
  key: String,
  defaultValue: Float,
): SavedStateHandleKey<Float> =
  key(key, defaultValue)

public fun SavedStateHandleKey.Companion.floatArray(
  key: String,
  defaultValue: FloatArray,
): SavedStateHandleKey<FloatArray> =
  key(key, defaultValue)

public fun <T : Parcelable> SavedStateHandleKey.Companion.parcelable(
  key: String,
  defaultValue: T,
): SavedStateHandleKey<T> =
  key(key, defaultValue)

public fun <T : Parcelable> SavedStateHandleKey.Companion.parcelableArray(
  key: String,
  defaultValue: Array<T>,
): SavedStateHandleKey<Array<T>> =
  key(key, defaultValue)

public fun SavedStateHandleKey.Companion.short(
  key: String,
  defaultValue: Short,
): SavedStateHandleKey<Short> =
  key(key, defaultValue)

public fun SavedStateHandleKey.Companion.shortArray(
  key: String,
  defaultValue: ShortArray,
): SavedStateHandleKey<ShortArray> =
  key(key, defaultValue)

public fun <T : Parcelable> SavedStateHandleKey.Companion.parcelableArrayList(
  key: String,
  defaultValue: ArrayList<T>,
): SavedStateHandleKey<ArrayList<T>> =
  key(key, defaultValue)

public fun SavedStateHandleKey.Companion.intArrayList(
  key: String,
  defaultValue: ArrayList<Int>,
): SavedStateHandleKey<ArrayList<Int>> =
  key(key, defaultValue)

public fun SavedStateHandleKey.Companion.stringArrayList(
  key: String,
  defaultValue: ArrayList<String>,
): SavedStateHandleKey<ArrayList<String>> =
  key(key, defaultValue)

public fun SavedStateHandleKey.Companion.charSequenceArrayList(
  key: String,
  defaultValue: ArrayList<CharSequence>,
): SavedStateHandleKey<ArrayList<CharSequence>> =
  key(key, defaultValue)
