package com.hoc081098.kmp.viewmodel.koin.compose

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.CreationExtrasKey
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.VIEW_MODEL_KEY
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelStore
import com.hoc081098.kmp.viewmodel.ViewModelStoreOwner
import com.hoc081098.kmp.viewmodel.buildCreationExtras
import junit.framework.TestCase.assertSame
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.koin.compose.KoinContext
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.logger.Level
import org.koin.core.module.dsl.factoryOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

private class TestViewModel(
  val extras: CreationExtras,
  val savedStateHandle: SavedStateHandle,
  val int: Int,
  val string: String,
) : ViewModel() {
  init {
    println(">>> $this::init with extras=$extras, savedStateHandle=$savedStateHandle, int=$int, string=$string")
  }
}

private class FakeViewModelStoreOwner : ViewModelStoreOwner {
  val store = ViewModelStore()

  override val viewModelStore: ViewModelStore = store
}

@JvmField
internal val TestModule = module {
  factoryOf(::TestViewModel)
}

class KoinKmpViewModelTest {
  @get:Rule
  val composeTestRule: ComposeContentTestRule = createComposeRule()

  private lateinit var koin: Koin

  @BeforeTest
  fun setup() {
    koin = startKoin {
      modules(TestModule)
      printLogger(Level.DEBUG)
    }.koin
  }

  @AfterTest
  fun tearDown() {
    stopKoin()
  }

  @Test
  fun test() {
    val owner = FakeViewModelStoreOwner()
    val savedStateHandle = SavedStateHandle(
      mapOf(
        "int" to 1,
        "string" to "string",
      ),
    )
    val extraKey = object : CreationExtrasKey<String> {}
    val extras = buildCreationExtras { this[extraKey] = "hoc081098" }

    var viewModel1: TestViewModel? = null
    var viewModel2: TestViewModel? = null

    composeTestRule.setContent {
      KoinContext(koin) {
        viewModel1 = koinKmpViewModel<TestViewModel>(
          viewModelStoreOwner = owner,
          savedStateHandleFactory = { savedStateHandle },
          extras = extras,
          parameters = { parametersOf(1998, "God is love") },
        )
        viewModel2 = koinKmpViewModel<TestViewModel>(
          viewModelStoreOwner = owner,
          savedStateHandleFactory = { savedStateHandle },
          extras = extras,
          parameters = { parametersOf(1998, "God is love") },
        )
      }
    }

    assertNotNull(viewModel1)
    assertSame(viewModel1, viewModel2)

    // Check savedStateHandle
    assertSame(savedStateHandle, viewModel2!!.savedStateHandle)

    // Check extras
    assertEquals(
      expected = "androidx.lifecycle.ViewModelProvider.DefaultKey:com.hoc081098.kmp.viewmodel.koin.compose.TestViewModel",
      actual = viewModel2!!.extras[VIEW_MODEL_KEY],
    )
    assertEquals(
      expected = "hoc081098",
      actual = viewModel2!!.extras[extraKey],
    )
  }
}
