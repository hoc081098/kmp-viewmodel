@file:Suppress("TooManyFunctions")

package com.hoc081098.kmp.viewmodel.safe

import com.hoc081098.kmp.viewmodel.InternalKmpViewModelApi
import com.hoc081098.kmp.viewmodel.parcelable.Parcelable
import com.hoc081098.kmp.viewmodel.safe.internal.parcelableArrayTransform
import com.hoc081098.kmp.viewmodel.serializable.JvmSerializable

@OptIn(InternalKmpViewModelApi::class)
@Suppress("NOTHING_TO_INLINE")
private inline fun <T : Any> key(key: String, value: T) = NonNullSavedStateHandleKey(key, value)

public fun NonNullSavedStateHandleKey.Companion.boolean(
  key: String,
  defaultValue: Boolean,
): NonNullSavedStateHandleKey<Boolean> =
  key(key, defaultValue)

public fun NonNullSavedStateHandleKey.Companion.booleanArray(
  key: String,
  defaultValue: BooleanArray,
): NonNullSavedStateHandleKey<BooleanArray> =
  key(key, defaultValue)

public fun NonNullSavedStateHandleKey.Companion.double(
  key: String,
  defaultValue: Double,
): NonNullSavedStateHandleKey<Double> =
  key(key, defaultValue)

public fun NonNullSavedStateHandleKey.Companion.doubleArray(
  key: String,
  defaultValue: DoubleArray,
): NonNullSavedStateHandleKey<DoubleArray> =
  key(key, defaultValue)

public fun NonNullSavedStateHandleKey.Companion.int(
  key: String,
  defaultValue: Int,
): NonNullSavedStateHandleKey<Int> =
  key(key, defaultValue)

public fun NonNullSavedStateHandleKey.Companion.intArray(
  key: String,
  defaultValue: IntArray,
): NonNullSavedStateHandleKey<IntArray> =
  key(key, defaultValue)

public fun NonNullSavedStateHandleKey.Companion.long(
  key: String,
  defaultValue: Long,
): NonNullSavedStateHandleKey<Long> =
  key(key, defaultValue)

public fun NonNullSavedStateHandleKey.Companion.longArray(
  key: String,
  defaultValue: LongArray,
): NonNullSavedStateHandleKey<LongArray> =
  key(key, defaultValue)

public fun NonNullSavedStateHandleKey.Companion.string(
  key: String,
  defaultValue: String,
): NonNullSavedStateHandleKey<String> =
  key(key, defaultValue)

public fun NonNullSavedStateHandleKey.Companion.stringArray(
  key: String,
  defaultValue: Array<String>,
): NonNullSavedStateHandleKey<Array<String>> =
  key(key, defaultValue)

public fun NonNullSavedStateHandleKey.Companion.byte(
  key: String,
  defaultValue: Byte,
): NonNullSavedStateHandleKey<Byte> =
  key(key, defaultValue)

public fun NonNullSavedStateHandleKey.Companion.byteArray(
  key: String,
  defaultValue: ByteArray,
): NonNullSavedStateHandleKey<ByteArray> =
  key(key, defaultValue)

public fun NonNullSavedStateHandleKey.Companion.char(
  key: String,
  defaultValue: Char,
): NonNullSavedStateHandleKey<Char> =
  key(key, defaultValue)

public fun NonNullSavedStateHandleKey.Companion.charArray(
  key: String,
  defaultValue: CharArray,
): NonNullSavedStateHandleKey<CharArray> =
  key(key, defaultValue)

public fun NonNullSavedStateHandleKey.Companion.charSequence(
  key: String,
  defaultValue: CharSequence,
): NonNullSavedStateHandleKey<CharSequence> =
  key(key, defaultValue)

public fun NonNullSavedStateHandleKey.Companion.charSequenceArray(
  key: String,
  defaultValue: Array<CharSequence?>,
): NonNullSavedStateHandleKey<Array<CharSequence?>> =
  key(key, defaultValue)

public fun NonNullSavedStateHandleKey.Companion.float(
  key: String,
  defaultValue: Float,
): NonNullSavedStateHandleKey<Float> =
  key(key, defaultValue)

public fun NonNullSavedStateHandleKey.Companion.floatArray(
  key: String,
  defaultValue: FloatArray,
): NonNullSavedStateHandleKey<FloatArray> =
  key(key, defaultValue)

public fun <T : JvmSerializable> NonNullSavedStateHandleKey.Companion.serializable(
  key: String,
  defaultValue: T,
): NonNullSavedStateHandleKey<T> =
  key(key, defaultValue)

public fun <T : Parcelable> NonNullSavedStateHandleKey.Companion.parcelable(
  key: String,
  defaultValue: T,
): NonNullSavedStateHandleKey<T> =
  key(key, defaultValue)

@OptIn(InternalKmpViewModelApi::class)
public inline fun <reified T : Parcelable> NonNullSavedStateHandleKey.Companion.parcelableArray(
  key: String,
  defaultValue: Array<T?>,
): NonNullSavedStateHandleKey<Array<T?>> =
  NonNullSavedStateHandleKey(
    key = key,
    defaultValue = defaultValue,
    transform = parcelableArrayTransform<T>(),
  )

public fun NonNullSavedStateHandleKey.Companion.short(
  key: String,
  defaultValue: Short,
): NonNullSavedStateHandleKey<Short> =
  key(key, defaultValue)

public fun NonNullSavedStateHandleKey.Companion.shortArray(
  key: String,
  defaultValue: ShortArray,
): NonNullSavedStateHandleKey<ShortArray> =
  key(key, defaultValue)

public fun <T : Parcelable> NonNullSavedStateHandleKey.Companion.parcelableArrayList(
  key: String,
  defaultValue: ArrayList<T?>,
): NonNullSavedStateHandleKey<ArrayList<T?>> =
  key(key, defaultValue)

public fun NonNullSavedStateHandleKey.Companion.intArrayList(
  key: String,
  defaultValue: ArrayList<Int?>,
): NonNullSavedStateHandleKey<ArrayList<Int?>> =
  key(key, defaultValue)

public fun NonNullSavedStateHandleKey.Companion.stringArrayList(
  key: String,
  defaultValue: ArrayList<String?>,
): NonNullSavedStateHandleKey<ArrayList<String?>> =
  key(key, defaultValue)

public fun NonNullSavedStateHandleKey.Companion.charSequenceArrayList(
  key: String,
  defaultValue: ArrayList<CharSequence?>,
): NonNullSavedStateHandleKey<ArrayList<CharSequence?>> =
  key(key, defaultValue)
