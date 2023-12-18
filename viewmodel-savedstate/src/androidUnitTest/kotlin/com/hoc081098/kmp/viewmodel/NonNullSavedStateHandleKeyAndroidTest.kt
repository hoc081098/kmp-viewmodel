package com.hoc081098.kmp.viewmodel

import androidx.lifecycle.LiveData
import com.hoc081098.kmp.viewmodel.safe.DelicateSafeSavedStateHandleApi
import com.hoc081098.kmp.viewmodel.safe.NonNullSavedStateHandleKey
import com.hoc081098.kmp.viewmodel.safe.safe
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(DelicateSafeSavedStateHandleApi::class)
@RunWith(RobolectricTestRunner::class)
class NonNullSavedStateHandleKeyAndroidTest {
  @Test
  fun readWriteRead() {
    val savedStateHandle = SavedStateHandle()

    nonNullKeyAndNextValues.forEach { (key, nextValue) ->
      savedStateHandle.safe { safeSavedStateHandle ->
        // Read
        assertNull(savedStateHandle[key.key])
        assertFalse { key.key in savedStateHandle }

        assertEquals(key.defaultValue, safeSavedStateHandle[key])
        assertNull(savedStateHandle[key.key])

        // Update
        @Suppress("UNCHECKED_CAST")
        safeSavedStateHandle[key as NonNullSavedStateHandleKey<Any>] = nextValue

        // Read
        assertTrue { key.key in savedStateHandle }

        assertEquals(nextValue, safeSavedStateHandle[key])
        assertEquals(nextValue, savedStateHandle[key.key])
      }
    }

    // Convert to bundle to check
    val bundle = savedStateHandle.savedStateProvider().saveState()
    val restored = SavedStateHandle.createHandle(bundle, null)

    nonNullKeyAndNextValues.forEach { (key, nextValue) ->
      assertEquals(nextValue, restored[key.key])
      assertEquals(nextValue, restored.safe { it[key] })
    }
  }

  @Test
  fun getLiveData_noExistingValue() {
    val savedStateHandle = SavedStateHandle()

    nonNullKeyAndNextValues.forEach { (key) ->
      @Suppress("UNCHECKED_CAST")
      val liveData: LiveData<Any> = savedStateHandle.safe { it.getLiveData(key) } as LiveData<Any>
      liveData.assertValue(key.defaultValue)
    }
  }

  @Test
  fun getLiveData_existingValue() {
    val savedStateHandle = SavedStateHandle()

    nonNullKeyAndNextValues.forEach { (key, nextValue) ->
      savedStateHandle[key.key] = nextValue

      @Suppress("UNCHECKED_CAST")
      val liveData: LiveData<Any> = savedStateHandle.safe { it.getLiveData(key) } as LiveData<Any>

      liveData.assertValue(nextValue)
    }
  }

  @Test
  fun getLiveData_noExistingValue_set() = runTest {
    val savedStateHandle = SavedStateHandle()

    nonNullKeyAndNextValues.forEach { (key, nextValue) ->
      @Suppress("UNCHECKED_CAST")
      val liveData: LiveData<Any> = savedStateHandle.safe { it.getLiveData(key) } as LiveData<Any>

      val job = launch {
        delay(1)

        @Suppress("UNCHECKED_CAST")
        savedStateHandle.safe { it[key as NonNullSavedStateHandleKey<Any>] = nextValue }
      }

      liveData.assertValues(key.defaultValue, nextValue)

      job.join()
    }
  }

  @Test
  fun getLiveData_existingValue_set() = runTest {
    val savedStateHandle = SavedStateHandle()

    nonNullKeyAndNextValues.forEach { (key, nextValue) ->
      savedStateHandle[key.key] = nextValue

      @Suppress("UNCHECKED_CAST")
      val liveData: LiveData<Any> = savedStateHandle.safe { it.getLiveData(key) } as LiveData<Any>

      val job = launch {
        delay(1)

        @Suppress("UNCHECKED_CAST")
        savedStateHandle.safe { it[key as NonNullSavedStateHandleKey<Any>] = key.defaultValue }
      }

      liveData.assertValues(nextValue, key.defaultValue)

      job.join()
    }
  }
}
