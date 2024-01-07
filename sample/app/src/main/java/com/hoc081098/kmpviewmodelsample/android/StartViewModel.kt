package com.hoc081098.kmpviewmodelsample.android

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoc081098.kmp.viewmodel.safe.NullableSavedStateHandleKey
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
  private val stateFlow = savedStateHandle.safe { it.getStateFlow(PARCELABLE_ARRAY_KEY) }

  init {
    Napier.d("$this::init")
    playground()
  }

  private fun playground() {
    // Observe
    stateFlow
      .onEach { Napier.d("$this:: stateFlow=${it.contentToString()}") }
      .launchIn(viewModelScope)

    // Read
    savedStateHandle
      .safe { it[PARCELABLE_ARRAY_KEY] }
      .let { Napier.d("$this: saved=${it.contentToString()}") }

    // Write
    savedStateHandle
      .safe {
        it[PARCELABLE_ARRAY_KEY] = arrayOf(
          TestParcelable(System.currentTimeMillis()),
          null,
          TestParcelable(System.currentTimeMillis()),
        )
        it[PARCELABLE_ARRAY_KEY]
      }
      .let { Napier.d("$this: updated=${it.contentToString()}") }
  }

  private companion object {
    private val PARCELABLE_ARRAY_KEY = NullableSavedStateHandleKey.parcelableArray<TestParcelable>("parcelableArrayKey")
  }
}
