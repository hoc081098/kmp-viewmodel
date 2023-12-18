package com.hoc081098.kmp.viewmodel

import com.hoc081098.kmp.viewmodel.safe.DelicateSafeSavedStateHandleApi
import com.hoc081098.kmp.viewmodel.safe.NonNullSavedStateHandleKey
import com.hoc081098.kmp.viewmodel.safe.safe
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
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
}
