package com.hoc081098.kmp.viewmodel

import com.hoc081098.kmp.viewmodel.parcelable.Parcelable

public fun SavedStateHandleKey.Companion.nullableBoolean(
  key: String,
  defaultValue: Boolean? = null,
): SavedStateHandleKey<Boolean?> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.nullableBooleanArray(
  key: String,
  defaultValue: BooleanArray? = null,
): SavedStateHandleKey<BooleanArray?> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.nullableDouble(
  key: String,
  defaultValue: Double? = null,
): SavedStateHandleKey<Double?> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.nullableDoubleArray(
  key: String,
  defaultValue: DoubleArray? = null,
): SavedStateHandleKey<DoubleArray?> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.nullableInt(
  key: String,
  defaultValue: Int? = null,
): SavedStateHandleKey<Int?> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.nullableIntArray(
  key: String,
  defaultValue: IntArray? = null,
): SavedStateHandleKey<IntArray?> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.nullableLong(
  key: String,
  defaultValue: Long? = null
): SavedStateHandleKey<Long?> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.nullableLongArray(
  key: String,
  defaultValue: LongArray? = null,
): SavedStateHandleKey<LongArray?> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.nullableString(
  key: String,
  defaultValue: String? = null,
): SavedStateHandleKey<String?> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.nullableStringArray(
  key: String,
  defaultValue: Array<String>? = null,
): SavedStateHandleKey<Array<String>?> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.nullableByte(
  key: String,
  defaultValue: Byte? = null,
): SavedStateHandleKey<Byte?> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.nullableByteArray(
  key: String,
  defaultValue: ByteArray? = null,
): SavedStateHandleKey<ByteArray?> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.nullableChar(
  key: String,
  defaultValue: Char? = null,
): SavedStateHandleKey<Char?> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.nullableCharArray(
  key: String,
  defaultValue: CharArray? = null,
): SavedStateHandleKey<CharArray?> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.nullableCharSequence(
  key: String,
  defaultValue: CharSequence? = null,
): SavedStateHandleKey<CharSequence?> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.nullableCharSequenceArray(
  key: String,
  defaultValue: Array<CharSequence>? = null,
): SavedStateHandleKey<Array<CharSequence>?> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.nullableFloat(
  key: String,
  defaultValue: Float? = null,
): SavedStateHandleKey<Float?> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.nullableFloatArray(
  key: String,
  defaultValue: FloatArray? = null,
): SavedStateHandleKey<FloatArray?> =
  SavedStateHandleKey(key, defaultValue)

public fun <T : Parcelable> SavedStateHandleKey.Companion.nullableParcelable(
  key: String,
  defaultValue: T? = null,
): SavedStateHandleKey<T?> =
  SavedStateHandleKey(key, defaultValue)

public fun <T : Parcelable> SavedStateHandleKey.Companion.nullableParcelableArray(
  key: String,
  defaultValue: Array<T>? = null,
): SavedStateHandleKey<Array<T>?> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.nullableShort(
  key: String,
  defaultValue: Short? = null,
): SavedStateHandleKey<Short?> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.nullableShortArray(
  key: String,
  defaultValue: ShortArray? = null,
): SavedStateHandleKey<ShortArray?> =
  SavedStateHandleKey(key, defaultValue)

public fun <T : Parcelable> SavedStateHandleKey.Companion.nullableParcelableArrayList(
  key: String,
  defaultValue: ArrayList<T>? = null,
): SavedStateHandleKey<ArrayList<T>?> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.nullableIntArrayList(
  key: String,
  defaultValue: ArrayList<Int>? = null,
): SavedStateHandleKey<ArrayList<Int>?> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.nullableStringArrayList(
  key: String,
  defaultValue: ArrayList<String>? = null,
): SavedStateHandleKey<ArrayList<String>?> =
  SavedStateHandleKey(key, defaultValue)

public fun SavedStateHandleKey.Companion.nullableCharSequenceArrayList(
  key: String,
  defaultValue: ArrayList<CharSequence>? = null,
): SavedStateHandleKey<ArrayList<CharSequence>?> =
  SavedStateHandleKey(key, defaultValue)
