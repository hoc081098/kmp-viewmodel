package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.ui.test.junit4.createComposeRule
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
import kotlin.test.assertNotNull
import kotlin.test.assertSame
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
  @OptIn(InternalKmpViewModelApi::class)
  val store = ViewModelStore(androidx.lifecycle.ViewModelStore())

  override val viewModelStore: ViewModelStore = store
}

class KmpViewModelTest {
  @get:Rule
  val composeTestRule = createComposeRule()

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
}
