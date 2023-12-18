package com.hoc081098.kmp.viewmodel

import androidx.lifecycle.LiveData
import com.hoc081098.kmp.viewmodel.safe.DelicateSafeSavedStateHandleApi
import com.hoc081098.kmp.viewmodel.safe.NullableSavedStateHandleKey
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
class NullableSavedStateHandleKeyAndroidTest {
  @Test
  fun readWriteRead() {
    val savedStateHandle = SavedStateHandle()

    nullableKeyAndNextValues.forEach { (key, nextValue) ->
      savedStateHandle.safe { safeSavedStateHandle ->
        // Read
        assertNull(savedStateHandle[key.key])
        assertFalse { key.key in savedStateHandle }

        assertEquals(key.defaultValue, safeSavedStateHandle[key])
        assertNull(savedStateHandle[key.key])

        // Update
        @Suppress("UNCHECKED_CAST")
        safeSavedStateHandle[key as NullableSavedStateHandleKey<Any>] = nextValue

        // Read
        assertTrue { key.key in savedStateHandle }

        assertEquals(nextValue, safeSavedStateHandle[key])
        assertEquals(nextValue, savedStateHandle[key.key])
      }
    }

    // Convert to bundle to check
    val bundle = savedStateHandle.savedStateProvider().saveState()
    val restored = SavedStateHandle.createHandle(bundle, null)

    nullableKeyAndNextValues.forEach { (key, nextValue) ->
      assertEquals(nextValue, restored[key.key])
      assertEquals(nextValue, restored.safe { it[key] })
    }
  }

  @Test
  fun readRemoveRead() {
    val savedStateHandle = SavedStateHandle()

    nullableKeyAndNextValues.forEach { (key) ->
      savedStateHandle.safe { safeSavedStateHandle ->
        // Read
        assertNull(savedStateHandle[key.key])
        assertFalse { key.key in savedStateHandle }

        assertEquals(key.defaultValue, safeSavedStateHandle[key])
        assertNull(savedStateHandle[key.key])

        // Remove
        safeSavedStateHandle.remove(key)

        // Read
        assertFalse { key.key in savedStateHandle }

        assertEquals(key.defaultValue, safeSavedStateHandle[key])
        assertNull(savedStateHandle[key.key])
      }
    }

    // Convert to bundle to check
    val bundle = savedStateHandle.savedStateProvider().saveState()
    val restored = SavedStateHandle.createHandle(bundle, null)

    nullableKeyAndNextValues.forEach { (key, nextValue) ->
      assertNull(restored[key.key])
      assertEquals(key.defaultValue, restored.safe { it[key] })
    }
  }

  @Test
  fun getLiveData_noExistingValue() {
    val savedStateHandle = SavedStateHandle()

    nullableKeyAndNextValues.forEach { (key) ->
      @Suppress("UNCHECKED_CAST")
      val liveData: LiveData<Any> = savedStateHandle.safe { it.getLiveData(key) } as LiveData<Any>
      liveData.assertValue(key.defaultValue)
    }
  }

  @Test
  fun getLiveData_existingValue() {
    val savedStateHandle = SavedStateHandle()

    nullableKeyAndNextValues.forEach { (key, nextValue) ->
      savedStateHandle[key.key] = nextValue

      @Suppress("UNCHECKED_CAST")
      val liveData: LiveData<Any?> = savedStateHandle.safe { it.getLiveData(key) } as LiveData<Any?>

      liveData.assertValue(nextValue)
    }
  }

  @Test
  fun getLiveData_existingNullValue() {
    val savedStateHandle = SavedStateHandle()

    nullableKeyAndNextValues.forEach { (key, nextValue) ->
      savedStateHandle[key.key] = null

      @Suppress("UNCHECKED_CAST")
      val liveData: LiveData<Any?> = savedStateHandle.safe { it.getLiveData(key) } as LiveData<Any?>

      liveData.assertValue(null)
    }
  }

  @Test
  fun getLiveData_noExistingValue_set() = runTest {
    val savedStateHandle = SavedStateHandle()

    nullableKeyAndNextValues.forEach { (key, nextValue) ->
      @Suppress("UNCHECKED_CAST")
      val liveData: LiveData<Any?> = savedStateHandle.safe { it.getLiveData(key) } as LiveData<Any?>

      val job = launch {
        delay(1)

        @Suppress("UNCHECKED_CAST")
        savedStateHandle.safe { it[key as NullableSavedStateHandleKey<Any>] = nextValue }
      }

      liveData.assertValues(key.defaultValue, nextValue)

      job.join()
    }
  }

  @Test
  fun getLiveData_existingValue_set() = runTest {
    val savedStateHandle = SavedStateHandle()

    nullableKeyAndNextValues.forEach { (key, nextValue) ->
      savedStateHandle[key.key] = nextValue

      @Suppress("UNCHECKED_CAST")
      val liveData: LiveData<Any?> = savedStateHandle.safe { it.getLiveData(key) } as LiveData<Any?>

      val job = launch {
        delay(1)

        @Suppress("UNCHECKED_CAST")
        savedStateHandle.safe { it[key as NullableSavedStateHandleKey<Any>] = key.defaultValue }
      }

      liveData.assertValues(nextValue, key.defaultValue)

      job.join()
    }
  }

  @Test
  fun getLiveData_existingNullValue_set() = runTest {
    val savedStateHandle = SavedStateHandle()

    nullableKeyAndNextValues.forEach { (key, nextValue) ->
      savedStateHandle[key.key] = null

      @Suppress("UNCHECKED_CAST")
      val liveData: LiveData<Any?> = savedStateHandle.safe { it.getLiveData(key) } as LiveData<Any?>

      val job = launch {
        delay(1)

        @Suppress("UNCHECKED_CAST")
        savedStateHandle.safe { it[key as NullableSavedStateHandleKey<Any>] = nextValue }
      }

      liveData.assertValues(null, nextValue)

      job.join()
    }
  }
}
