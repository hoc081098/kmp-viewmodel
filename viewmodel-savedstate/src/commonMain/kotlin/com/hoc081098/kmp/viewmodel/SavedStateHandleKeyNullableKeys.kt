package com.hoc081098.kmp.viewmodel

import com.hoc081098.kmp.viewmodel.parcelable.Parcelable
import kotlin.jvm.JvmName

@JvmName("nullableBooleanArrayKey")
public fun nullableBooleanArraySavedStateHandleKey(
  key: String,
  defaultValue: BooleanArray? = null
): SavedStateHandleKey<BooleanArray?> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("nullableDoubleArrayKey")
public fun nullableDoubleArraySavedStateHandleKey(
  key: String,
  defaultValue: DoubleArray? = null
): SavedStateHandleKey<DoubleArray?> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("nullableLongArrayKey")
public fun nullableLongArraySavedStateHandleKey(
  key: String,
  defaultValue: LongArray? = null
): SavedStateHandleKey<LongArray?> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("nullableStringKey")
public fun nullableStringSavedStateHandleKey(key: String, defaultValue: String? = null): SavedStateHandleKey<String?> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("nullableStringArrayKey")
public fun nullableStringArraySavedStateHandleKey(
  key: String,
  defaultValue: Array<String>? = null
): SavedStateHandleKey<Array<String>?> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("nullableByteArrayKey")
public fun nullableByteArraySavedStateHandleKey(
  key: String,
  defaultValue: ByteArray? = null
): SavedStateHandleKey<ByteArray?> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("nullableCharArrayKey")
public fun nullableCharArraySavedStateHandleKey(
  key: String,
  defaultValue: CharArray? = null
): SavedStateHandleKey<CharArray?> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("nullableCharSequenceKey")
public fun nullableCharSequenceSavedStateHandleKey(
  key: String,
  defaultValue: CharSequence? = null
): SavedStateHandleKey<CharSequence?> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("nullableCharSequenceArrayKey")
public fun nullableCharSequenceArraySavedStateHandleKey(
  key: String,
  defaultValue: Array<CharSequence>? = null
): SavedStateHandleKey<Array<CharSequence>?> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("nullableFloatArrayKey")
public fun nullableFloatArraySavedStateHandleKey(
  key: String,
  defaultValue: FloatArray? = null
): SavedStateHandleKey<FloatArray?> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("nullableParcelableKey")
public fun <T : Parcelable> nullableParcelableSavedStateHandleKey(
  key: String,
  defaultValue: T? = null
): SavedStateHandleKey<T?> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("nullableParcelableArrayKey")
public fun <T : Parcelable> nullableParcelableArraySavedStateHandleKey(
  key: String,
  defaultValue: Array<T>? = null
): SavedStateHandleKey<Array<T>?> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("nullableShortArrayKey")
public fun nullableShortArraySavedStateHandleKey(
  key: String,
  defaultValue: ShortArray? = null
): SavedStateHandleKey<ShortArray?> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("nullableParcelableArrayListKey")
public fun <T : Parcelable> nullableParcelableArrayListSavedStateHandleKey(
  key: String,
  defaultValue: ArrayList<T>? = null
): SavedStateHandleKey<ArrayList<T>?> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("nullableIntArrayListKey")
public fun nullableIntArrayListSavedStateHandleKey(
  key: String,
  defaultValue: ArrayList<Int>? = null
): SavedStateHandleKey<ArrayList<Int>?> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("nullableStringArrayListKey")
public fun nullableStringArrayListSavedStateHandleKey(
  key: String,
  defaultValue: ArrayList<String>? = null
): SavedStateHandleKey<ArrayList<String>?> =
  SavedStateHandleKey(key, defaultValue)

@JvmName("nullableCharSequenceArrayListKey")
public fun nullableCharSequenceArrayListSavedStateHandleKey(
  key: String,
  defaultValue: ArrayList<CharSequence>? = null
): SavedStateHandleKey<ArrayList<CharSequence>?> =
  SavedStateHandleKey(key, defaultValue)
