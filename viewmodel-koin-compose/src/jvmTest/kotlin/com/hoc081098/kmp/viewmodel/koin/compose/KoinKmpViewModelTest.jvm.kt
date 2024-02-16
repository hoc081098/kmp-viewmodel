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
import com.hoc081098.kmp.viewmodel.compose.SavedStateHandleFactoryProvider
import com.hoc081098.kmp.viewmodel.compose.ViewModelStoreOwnerProvider
import junit.framework.TestCase.assertSame
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNotSame
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

  private val parameters = parametersOf(1998, "God is love")

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
  fun viewModelCreatedViaFactory() {
    val owner = FakeViewModelStoreOwner()
    val savedStateHandle = SavedStateHandle(
      mapOf(
        "int" to 1,
        "string" to "string",
      ),
    )
    val extraKey = object : CreationExtrasKey<String> {}
    val extras = buildCreationExtras { this[extraKey] = "hoc081098" }

    var createdInComposition1: TestViewModel? = null
    var createdInComposition2: TestViewModel? = null

    composeTestRule.setContent {
      KoinContext(koin) {
        createdInComposition1 = koinKmpViewModel<TestViewModel>(
          viewModelStoreOwner = owner,
          savedStateHandleFactory = { savedStateHandle },
          extras = extras,
          parameters = { parameters },
        )
        createdInComposition2 = koinKmpViewModel<TestViewModel>(
          viewModelStoreOwner = owner,
          savedStateHandleFactory = { savedStateHandle },
          extras = extras,
          parameters = { parameters },
        )
      }
    }

    assertNotNull(createdInComposition1)
    assertSame(createdInComposition1, createdInComposition2)

    // Check savedStateHandle
    assertSame(savedStateHandle, createdInComposition2!!.savedStateHandle)

    // Check extras
    assertEquals(
      expected = "androidx.lifecycle.ViewModelProvider.DefaultKey:com.hoc081098.kmp.viewmodel.koin.compose.TestViewModel",
      actual = createdInComposition2!!.extras[VIEW_MODEL_KEY],
    )
    assertEquals(
      expected = "hoc081098",
      actual = createdInComposition2!!.extras[extraKey],
    )

    // Check parameters
    assertEquals(
      expected = parameters.get<Int>(),
      actual = createdInComposition2!!.int,
    )
    assertEquals(
      expected = parameters.get<String>(),
      actual = createdInComposition2!!.string,
    )
  }

  @Test
  fun viewModelCreatedViaFactoryViaLocals() {
    val owner = FakeViewModelStoreOwner()
    val savedStateHandle = SavedStateHandle(
      mapOf(
        "int" to 1,
        "string" to "string",
      ),
    )
    val extraKey = object : CreationExtrasKey<String> {}
    val extras = buildCreationExtras { this[extraKey] = "hoc081098" }

    var createdInComposition1: TestViewModel? = null
    var createdInComposition2: TestViewModel? = null

    composeTestRule.setContent {
      KoinContext(koin) {
        ViewModelStoreOwnerProvider(viewModelStoreOwner = owner) {
          SavedStateHandleFactoryProvider(savedStateHandleFactory = { savedStateHandle }) {
            createdInComposition1 = koinKmpViewModel<TestViewModel>(
              extras = extras,
              parameters = { parameters },
            )
            createdInComposition2 = koinKmpViewModel<TestViewModel>(
              extras = extras,
              parameters = { parameters },
            )
          }
        }
      }
    }

    assertNotNull(createdInComposition1)
    assertSame(createdInComposition1, createdInComposition2)

    // Check savedStateHandle
    assertSame(savedStateHandle, createdInComposition2!!.savedStateHandle)

    // Check extras
    assertEquals(
      expected = "androidx.lifecycle.ViewModelProvider.DefaultKey:com.hoc081098.kmp.viewmodel.koin.compose.TestViewModel",
      actual = createdInComposition2!!.extras[VIEW_MODEL_KEY],
    )
    assertEquals(
      expected = "hoc081098",
      actual = createdInComposition2!!.extras[extraKey],
    )

    // Check parameters
    assertEquals(
      expected = parameters.get<Int>(),
      actual = createdInComposition2!!.int,
    )
    assertEquals(
      expected = parameters.get<String>(),
      actual = createdInComposition2!!.string,
    )
  }

  @Test
  fun viewModelCreatedViaFactoryWithKey() {
    val owner = FakeViewModelStoreOwner()
    val vmKey = "hoc081098"
    val otherVmKey = "hoc081098_other"
    val savedStateHandle = SavedStateHandle(
      mapOf(
        "int" to 1,
        "string" to "string",
      ),
    )
    val extraKey = object : CreationExtrasKey<String> {}
    val extras = buildCreationExtras { this[extraKey] = "hoc081098" }

    var createdInComposition1: TestViewModel? = null
    var createdInComposition2: TestViewModel? = null
    var createdInComposition3WithOtherKey: TestViewModel? = null

    composeTestRule.setContent {
      KoinContext(koin) {
        createdInComposition1 = koinKmpViewModel<TestViewModel>(
          key = vmKey,
          viewModelStoreOwner = owner,
          savedStateHandleFactory = { savedStateHandle },
          extras = extras,
          parameters = { parameters },
        )
        createdInComposition2 = koinKmpViewModel<TestViewModel>(
          key = vmKey,
          viewModelStoreOwner = owner,
          savedStateHandleFactory = { savedStateHandle },
          extras = extras,
          parameters = { parameters },
        )
        createdInComposition3WithOtherKey = koinKmpViewModel<TestViewModel>(
          key = otherVmKey,
          viewModelStoreOwner = owner,
          savedStateHandleFactory = { savedStateHandle },
          extras = extras,
          parameters = { parameters },
        )
      }
    }

    // Check created
    assertNotNull(createdInComposition1)
    assertNotNull(createdInComposition2)
    assertNotNull(createdInComposition3WithOtherKey)

    // Check same instance
    assertSame(createdInComposition1, createdInComposition2)
    assertNotSame(createdInComposition1, createdInComposition3WithOtherKey)

    // Check savedStateHandle
    assertSame(savedStateHandle, createdInComposition2!!.savedStateHandle)
    assertSame(savedStateHandle, createdInComposition3WithOtherKey!!.savedStateHandle)

    // Check extras
    assertEquals(
      expected = vmKey,
      actual = createdInComposition2!!.extras[VIEW_MODEL_KEY],
    )
    assertEquals(
      expected = otherVmKey,
      actual = createdInComposition3WithOtherKey!!.extras[VIEW_MODEL_KEY],
    )
    assertEquals(
      expected = "hoc081098",
      actual = createdInComposition2!!.extras[extraKey],
    )
    assertEquals(
      expected = "hoc081098",
      actual = createdInComposition3WithOtherKey!!.extras[extraKey],
    )

    // Check parameters
    assertEquals(
      expected = parameters.get<Int>(),
      actual = createdInComposition2!!.int,
    )
    assertEquals(
      expected = parameters.get<Int>(),
      actual = createdInComposition3WithOtherKey!!.int,
    )
    assertEquals(
      expected = parameters.get<String>(),
      actual = createdInComposition2!!.string,
    )
    assertEquals(
      expected = parameters.get<String>(),
      actual = createdInComposition3WithOtherKey!!.string,
    )
  }
}
