@file:Suppress("TooManyFunctions")

package com.hoc081098.kmp.viewmodel.safe

import com.hoc081098.kmp.viewmodel.InternalKmpViewModelApi
import com.hoc081098.kmp.viewmodel.parcelable.Parcelable
import com.hoc081098.kmp.viewmodel.safe.internal.nullableParcelableArrayTransform
import com.hoc081098.kmp.viewmodel.serializable.JvmSerializable

@OptIn(InternalKmpViewModelApi::class)
@Suppress("NOTHING_TO_INLINE")
private inline fun <T : Any> key(key: String, value: T?) = NullableSavedStateHandleKey(key, value)

public fun NullableSavedStateHandleKey.Companion.boolean(
  key: String,
  defaultValue: Boolean? = null,
): NullableSavedStateHandleKey<Boolean> =
  key(key, defaultValue)

public fun NullableSavedStateHandleKey.Companion.booleanArray(
  key: String,
  defaultValue: BooleanArray? = null,
): NullableSavedStateHandleKey<BooleanArray> =
  key(key, defaultValue)

public fun NullableSavedStateHandleKey.Companion.double(
  key: String,
  defaultValue: Double? = null,
): NullableSavedStateHandleKey<Double> =
  key(key, defaultValue)

public fun NullableSavedStateHandleKey.Companion.doubleArray(
  key: String,
  defaultValue: DoubleArray? = null,
): NullableSavedStateHandleKey<DoubleArray> =
  key(key, defaultValue)

public fun NullableSavedStateHandleKey.Companion.int(
  key: String,
  defaultValue: Int? = null,
): NullableSavedStateHandleKey<Int> =
  key(key, defaultValue)

public fun NullableSavedStateHandleKey.Companion.intArray(
  key: String,
  defaultValue: IntArray? = null,
): NullableSavedStateHandleKey<IntArray> =
  key(key, defaultValue)

public fun NullableSavedStateHandleKey.Companion.long(
  key: String,
  defaultValue: Long? = null,
): NullableSavedStateHandleKey<Long> =
  key(key, defaultValue)

public fun NullableSavedStateHandleKey.Companion.longArray(
  key: String,
  defaultValue: LongArray? = null,
): NullableSavedStateHandleKey<LongArray> =
  key(key, defaultValue)

public fun NullableSavedStateHandleKey.Companion.string(
  key: String,
  defaultValue: String? = null,
): NullableSavedStateHandleKey<String> =
  key(key, defaultValue)

public fun NullableSavedStateHandleKey.Companion.stringArray(
  key: String,
  defaultValue: Array<String?>? = null,
): NullableSavedStateHandleKey<Array<String?>> =
  key(key, defaultValue)

public fun NullableSavedStateHandleKey.Companion.byte(
  key: String,
  defaultValue: Byte? = null,
): NullableSavedStateHandleKey<Byte> =
  key(key, defaultValue)

public fun NullableSavedStateHandleKey.Companion.byteArray(
  key: String,
  defaultValue: ByteArray? = null,
): NullableSavedStateHandleKey<ByteArray> =
  key(key, defaultValue)

public fun NullableSavedStateHandleKey.Companion.char(
  key: String,
  defaultValue: Char? = null,
): NullableSavedStateHandleKey<Char> =
  key(key, defaultValue)

public fun NullableSavedStateHandleKey.Companion.charArray(
  key: String,
  defaultValue: CharArray? = null,
): NullableSavedStateHandleKey<CharArray> =
  key(key, defaultValue)

public fun NullableSavedStateHandleKey.Companion.charSequence(
  key: String,
  defaultValue: CharSequence? = null,
): NullableSavedStateHandleKey<CharSequence> =
  key(key, defaultValue)

public fun NullableSavedStateHandleKey.Companion.charSequenceArray(
  key: String,
  defaultValue: Array<CharSequence?>? = null,
): NullableSavedStateHandleKey<Array<CharSequence?>> =
  key(key, defaultValue)

public fun NullableSavedStateHandleKey.Companion.float(
  key: String,
  defaultValue: Float? = null,
): NullableSavedStateHandleKey<Float> =
  key(key, defaultValue)

public fun NullableSavedStateHandleKey.Companion.floatArray(
  key: String,
  defaultValue: FloatArray? = null,
): NullableSavedStateHandleKey<FloatArray> =
  key(key, defaultValue)

public fun <T : JvmSerializable> NullableSavedStateHandleKey.Companion.serializable(
  key: String,
  defaultValue: T? = null,
): NullableSavedStateHandleKey<T> =
  key(key, defaultValue)

public fun <T : Parcelable> NullableSavedStateHandleKey.Companion.parcelable(
  key: String,
  defaultValue: T? = null,
): NullableSavedStateHandleKey<T> =
  key(key, defaultValue)

@OptIn(InternalKmpViewModelApi::class)
public inline fun <reified T : Parcelable> NullableSavedStateHandleKey.Companion.parcelableArray(
  key: String,
  defaultValue: Array<T?>? = null,
): NullableSavedStateHandleKey<Array<T?>> = NullableSavedStateHandleKey(
  key = key,
  defaultValue = defaultValue,
  transform = nullableParcelableArrayTransform<T>(),
)

public fun NullableSavedStateHandleKey.Companion.short(
  key: String,
  defaultValue: Short? = null,
): NullableSavedStateHandleKey<Short> =
  key(key, defaultValue)

public fun NullableSavedStateHandleKey.Companion.shortArray(
  key: String,
  defaultValue: ShortArray? = null,
): NullableSavedStateHandleKey<ShortArray> =
  key(key, defaultValue)

public fun <T : Parcelable> NullableSavedStateHandleKey.Companion.parcelableArrayList(
  key: String,
  defaultValue: ArrayList<T?>? = null,
): NullableSavedStateHandleKey<ArrayList<T?>> =
  key(key, defaultValue)

public fun NullableSavedStateHandleKey.Companion.intArrayList(
  key: String,
  defaultValue: ArrayList<Int?>? = null,
): NullableSavedStateHandleKey<ArrayList<Int?>> =
  key(key, defaultValue)

public fun NullableSavedStateHandleKey.Companion.stringArrayList(
  key: String,
  defaultValue: ArrayList<String?>? = null,
): NullableSavedStateHandleKey<ArrayList<String?>> =
  key(key, defaultValue)

public fun NullableSavedStateHandleKey.Companion.charSequenceArrayList(
  key: String,
  defaultValue: ArrayList<CharSequence?>? = null,
): NullableSavedStateHandleKey<ArrayList<CharSequence?>> =
  key(key, defaultValue)
