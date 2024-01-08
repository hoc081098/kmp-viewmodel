package com.hoc081098.kmpviewmodelsample.android

import android.os.Parcelable
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoc081098.kmp.viewmodel.safe.NonNullSavedStateHandleKey
import com.hoc081098.kmp.viewmodel.safe.NullableSavedStateHandleKey
import com.hoc081098.kmp.viewmodel.safe.getLiveData
import com.hoc081098.kmp.viewmodel.safe.parcelableArray
import com.hoc081098.kmp.viewmodel.safe.safe
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class TestParcelable(val value: Long) : Parcelable

internal class StartViewModel(
  private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
  private val nullableStateFlow = savedStateHandle.safe { it.getStateFlow(NULLABLE_PARCELABLE_ARRAY_KEY) }
  private val nullableLiveData = savedStateHandle.safe.getLiveData(NULLABLE_PARCELABLE_ARRAY_KEY)
  private val nullableObserver = Observer<Array<TestParcelable?>?> {
    Napier.d("$this: NULLABLE_PARCELABLE_ARRAY_KEY nullableLiveData=${it.contentToString()}")
  }

  private val nonNullStateFlow = savedStateHandle.safe { it.getStateFlow(NON_NULL_PARCELABLE_ARRAY_KEY) }
  private val nonNullLiveData = savedStateHandle.safe.getLiveData(NON_NULL_PARCELABLE_ARRAY_KEY)
  private val nonNullObserver = Observer<Array<TestParcelable?>?> {
    Napier.d("$this: NON_NULL_PARCELABLE_ARRAY_KEY nonNullLiveData=${it.contentToString()}")
  }

  init {
    Napier.d("$this::init")

    playgroundNullableKey()
    playgroundNonNullKey()
  }

  private fun playgroundNullableKey() {
    // Observe
    nullableStateFlow
      .onEach { Napier.d("$this: NULLABLE_PARCELABLE_ARRAY_KEY nullableStateFlow=${it.contentToString()}") }
      .launchIn(viewModelScope)

    nullableLiveData.observeForever(nullableObserver)
    addCloseable { nullableLiveData.removeObserver(nullableObserver) }

    // Read
    savedStateHandle
      .safe { it[NULLABLE_PARCELABLE_ARRAY_KEY] }
      .let { Napier.d("$this: NULLABLE_PARCELABLE_ARRAY_KEY saved=${it.contentToString()}") }

    // Write
    savedStateHandle
      .safe {
        it[NULLABLE_PARCELABLE_ARRAY_KEY] = arrayOf(
          TestParcelable(System.currentTimeMillis()),
          null,
          TestParcelable(System.currentTimeMillis()),
        )
        it[NULLABLE_PARCELABLE_ARRAY_KEY]
      }
      .let { Napier.d("$this: NULLABLE_PARCELABLE_ARRAY_KEY updated=${it.contentToString()}") }
  }

  private fun playgroundNonNullKey() {
    // Observe
    nonNullStateFlow
      .onEach { Napier.d("$this: NON_NULL_PARCELABLE_ARRAY_KEY nonNullStateFlow=${it.contentToString()}") }
      .launchIn(viewModelScope)

    nonNullLiveData.observeForever(nonNullObserver)
    addCloseable { nonNullLiveData.removeObserver(nonNullObserver) }

    // Read
    savedStateHandle
      .safe { it[NON_NULL_PARCELABLE_ARRAY_KEY] }
      .let { Napier.d("$this: NON_NULL_PARCELABLE_ARRAY_KEY saved=${it.contentToString()}") }

    // Write
    savedStateHandle
      .safe {
        it[NON_NULL_PARCELABLE_ARRAY_KEY] = arrayOf(
          TestParcelable(System.currentTimeMillis()),
          null,
          TestParcelable(System.currentTimeMillis()),
        )
        it[NON_NULL_PARCELABLE_ARRAY_KEY]
      }
      .let { Napier.d("$this: NON_NULL_PARCELABLE_ARRAY_KEY updated=${it.contentToString()}") }
  }

  private companion object {
    private val NULLABLE_PARCELABLE_ARRAY_KEY =
      NullableSavedStateHandleKey.parcelableArray<TestParcelable>("NULLABLE_PARCELABLE_ARRAY_KEY")

    private val NON_NULL_PARCELABLE_ARRAY_KEY = NonNullSavedStateHandleKey.parcelableArray(
      key = "NON_NULL_PARCELABLE_ARRAY_KEY",
      defaultValue = arrayOf(TestParcelable(1), null, TestParcelable(3)),
    )
  }
}
