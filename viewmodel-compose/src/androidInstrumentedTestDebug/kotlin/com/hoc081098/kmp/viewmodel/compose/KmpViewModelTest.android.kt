package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.ui.test.junit4.createComposeRule
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.InternalKmpViewModelApi
import com.hoc081098.kmp.viewmodel.VIEW_MODEL_KEY
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelStore
import com.hoc081098.kmp.viewmodel.ViewModelStoreOwner
import kotlin.test.assertNotNull
import kotlin.test.assertSame
import org.junit.Rule
import org.junit.Test

private class TestViewModel(
  val extras: CreationExtras,
) : ViewModel() {
  init {
    println("$this::init")
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
  fun default() {
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
    assertNotNull(createdInComposition1!!.extras[VIEW_MODEL_KEY])
  }
}
