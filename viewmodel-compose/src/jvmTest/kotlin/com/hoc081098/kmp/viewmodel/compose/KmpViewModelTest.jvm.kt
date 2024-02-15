package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.window.Dialog
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.CreationExtrasKey
import com.hoc081098.kmp.viewmodel.InternalKmpViewModelApi
import com.hoc081098.kmp.viewmodel.VIEW_MODEL_KEY
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelStore
import com.hoc081098.kmp.viewmodel.ViewModelStoreOwner
import com.hoc081098.kmp.viewmodel.buildCreationExtras
import com.hoc081098.kmp.viewmodel.edit
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue
import org.junit.Rule
import org.junit.Test

private class TestViewModel(
  val extras: CreationExtras,
) : ViewModel() {
  init {
    println(">>> $this::init with $extras")
  }
}

private class FakeViewModelStoreOwner : ViewModelStoreOwner {
  val store = ViewModelStore()

  override val viewModelStore: ViewModelStore = store
}

class KmpViewModelTest {
  @get:Rule
  val composeTestRule: ComposeContentTestRule = createComposeRule()

  @Test
  fun nullViewModelStoreOwnerViaLocalViewModelStoreOwner() {
    var owner: ViewModelStoreOwner? = null

    composeTestRule.setContent {
      Dialog(onDismissRequest = {}) {
        // This should return null because no LocalViewModelStoreOwner was set
        owner = LocalViewModelStoreOwner.current
      }
    }

    assertNull(owner)
  }

  @Test
  fun nonNullViewModelStoreOwnerViaDefaultViewModelStoreOwner() {
    var ownerViaLocalViewModelStoreOwner: ViewModelStoreOwner? = null
    var owner: ViewModelStoreOwner? = null

    composeTestRule.setContent {
      Dialog(onDismissRequest = {}) {
        // This should return null because no LocalViewModelStoreOwner was set
        ownerViaLocalViewModelStoreOwner = LocalViewModelStoreOwner.current

        // This should return non-null because it uses defaultPlatformViewModelStoreOwner as the fallback.
        owner = defaultViewModelStoreOwner()
      }
    }

    assertNull(ownerViaLocalViewModelStoreOwner)
    assertNotNull(owner)
  }

  @Test
  fun viewModelCreatedViaFactory() {
    val owner = FakeViewModelStoreOwner()
    var createdInComposition1: TestViewModel? = null
    var createdInComposition2: TestViewModel? = null

    composeTestRule.setContent {
      ViewModelStoreOwnerProvider(owner) {
        createdInComposition1 = kmpViewModel {
          TestViewModel(extras = this)
        }
        createdInComposition2 = kmpViewModel {
          TestViewModel(extras = this)
        }
      }
    }

    assertNotNull(createdInComposition1)
    assertNotNull(createdInComposition2)
    assertSame(createdInComposition1, createdInComposition2)
    assertEquals(
      expected = "androidx.lifecycle.ViewModelProvider.DefaultKey:com.hoc081098.kmp.viewmodel.compose.TestViewModel",
      actual = createdInComposition1!!.extras[VIEW_MODEL_KEY],
    )
  }

  @Test
  fun viewModelCreatedViaFactoryWithKey() {
    val owner = FakeViewModelStoreOwner()
    var createdInComposition1: TestViewModel? = null
    var createdInComposition2: TestViewModel? = null

    composeTestRule.setContent {
      ViewModelStoreOwnerProvider(owner) {
        createdInComposition1 = kmpViewModel(key = "test") {
          TestViewModel(extras = this)
        }
        createdInComposition2 = kmpViewModel(key = "test") {
          TestViewModel(extras = this)
        }
      }
    }

    assertNotNull(createdInComposition1)
    assertNotNull(createdInComposition2)
    assertSame(createdInComposition1, createdInComposition2)
    assertEquals(
      expected = "test",
      actual = createdInComposition1!!.extras[VIEW_MODEL_KEY],
    )
  }

  @Test
  fun viewModelCreatedViaFactoryWithKeyAndCreationExtras() {
    val owner = FakeViewModelStoreOwner()
    val extrasKey = object : CreationExtrasKey<String> {}
    val extras = buildCreationExtras {
      this[extrasKey] = "value"
    }

    var createdInComposition1: TestViewModel? = null
    var createdInComposition2: TestViewModel? = null

    composeTestRule.setContent {
      ViewModelStoreOwnerProvider(owner) {
        createdInComposition1 = kmpViewModel(key = "test", extras = extras) {
          TestViewModel(extras = this)
        }
        createdInComposition2 = kmpViewModel(key = "test", extras = extras) {
          TestViewModel(extras = this)
        }
      }
    }

    assertNotNull(createdInComposition1)
    assertNotNull(createdInComposition2)
    assertSame(createdInComposition1, createdInComposition2)

    assertEquals(
      expected = "test",
      actual = createdInComposition1!!.extras[VIEW_MODEL_KEY],
    )
    assertEquals(
      expected = "value",
      actual = createdInComposition1!!.extras[extrasKey],
    )
  }

  @Test
  fun viewModelCreatedCreationExtrasInitializer() {
    val owner = FakeViewModelStoreOwner()
    val extrasKey = object : CreationExtrasKey<String> {}

    var createdInComposition1: TestViewModel? = null
    var createdInComposition2: TestViewModel? = null

    composeTestRule.setContent {
      ViewModelStoreOwnerProvider(owner) {
        createdInComposition1 = kmpViewModel(key = "test") {
          TestViewModel(extras = edit { this[extrasKey] = "value" })
        }
        createdInComposition2 = kmpViewModel(key = "test") {
          TestViewModel(extras = edit { this[extrasKey] = "value" })
        }
      }
    }

    assertNotNull(createdInComposition1)
    assertNotNull(createdInComposition2)
    assertSame(createdInComposition1, createdInComposition2)

    assertEquals(
      expected = "test",
      actual = createdInComposition1!!.extras[VIEW_MODEL_KEY],
    )
    assertEquals(
      expected = "value",
      actual = createdInComposition1!!.extras[extrasKey],
    )
  }

  @OptIn(InternalKmpViewModelApi::class)
  @Test
  fun viewModelCreatedViaFactoryWithCustomOwner() {
    val customOwner = FakeViewModelStoreOwner()
    val owner = FakeViewModelStoreOwner()

    var createdInComposition1: TestViewModel? = null
    var createdInComposition2: TestViewModel? = null

    composeTestRule.setContent {
      ViewModelStoreOwnerProvider(owner) {
        createdInComposition1 = kmpViewModel(viewModelStoreOwner = customOwner) {
          TestViewModel(extras = this)
        }
        createdInComposition2 = kmpViewModel(viewModelStoreOwner = customOwner) {
          TestViewModel(extras = this)
        }
      }
    }

    assertNotNull(createdInComposition1)
    assertNotNull(createdInComposition2)
    assertSame(createdInComposition1, createdInComposition2)

    val expectedKey =
      "androidx.lifecycle.ViewModelProvider.DefaultKey:com.hoc081098.kmp.viewmodel.compose.TestViewModel"
    assertEquals(
      expected = expectedKey,
      actual = createdInComposition1!!.extras[VIEW_MODEL_KEY],
    )

    assertTrue {
      customOwner.store
        .keys()
        .contains(expectedKey)
    }
    assertFalse {
      owner.store
        .keys()
        .contains(expectedKey)
    }
  }
}
