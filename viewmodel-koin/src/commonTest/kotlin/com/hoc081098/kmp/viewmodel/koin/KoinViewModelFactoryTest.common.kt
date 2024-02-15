package com.hoc081098.kmp.viewmodel.koin

import com.hoc081098.kmp.viewmodel.CreationExtrasKey
import com.hoc081098.kmp.viewmodel.EmptyCreationExtras
import com.hoc081098.kmp.viewmodel.SAVED_STATE_HANDLE_FACTORY_KEY
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.SavedStateHandleFactory
import com.hoc081098.kmp.viewmodel.buildCreationExtras
import com.hoc081098.kmp.viewmodel.koin.test.TestSavedStateViewModel
import com.hoc081098.kmp.viewmodel.koin.test.TestViewModel
import com.hoc081098.kmp.viewmodel.koin.test.TestViewModelWithExtras
import com.hoc081098.kmp.viewmodel.koin.test.TestViewModelWithParams
import kotlin.jvm.JvmField
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertSame
import org.koin.core.Koin
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.logger.Level
import org.koin.core.module.dsl.factoryOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

@JvmField
internal val TestViewModelModule = module {
  factoryOf(::TestViewModel)
  factoryOf(::TestViewModelWithParams)
  factoryOf(::TestSavedStateViewModel)
  factoryOf(::TestViewModelWithExtras)
}

@OptIn(KoinInternalApi::class)
class KoinViewModelFactoryTest {
  private lateinit var koin: Koin

  @BeforeTest
  fun setUp() {
    koin = startKoin {
      modules(TestViewModelModule)
      printLogger(Level.DEBUG)
    }.koin
  }

  @AfterTest
  fun teardown() {
    stopKoin()
  }

  @Test
  fun createViewModelWithoutParams() {
    val factory = koinViewModelFactory<TestViewModel>(
      scope = koin.scopeRegistry.rootScope,
    )

    val viewModel = factory.create(EmptyCreationExtras)

    assertNotNull(viewModel)
  }

  @Test
  fun createViewModelWithParams() {
    val int = 1998
    val string = "God is love"

    val factory = koinViewModelFactory<TestViewModelWithParams>(
      scope = koin.scopeRegistry.rootScope,
      parameters = { parametersOf(int, string) },
    )

    val viewModel = factory.create(EmptyCreationExtras)

    assertNotNull(viewModel)
    assertEquals(expected = int, actual = viewModel.int)
    assertEquals(expected = string, actual = viewModel.string)
  }

  @Test
  fun createViewModelWithSavedStateHandle() {
    val key = "key"
    val value = "value"

    val factory = koinViewModelFactory<TestSavedStateViewModel>(
      scope = koin.scopeRegistry.rootScope,
    )

    val viewModel = factory.create(
      buildCreationExtras {
        this[SAVED_STATE_HANDLE_FACTORY_KEY] = SavedStateHandleFactory {
          SavedStateHandle(mapOf(key to value))
        }
      },
    )

    assertNotNull(viewModel)
    assertEquals(
      expected = value,
      actual = viewModel.savedStateHandle.get<String>(key),
    )
  }

  @Test
  fun createViewModelWithExtras() {
    val key = object : CreationExtrasKey<String> {}
    val value = "value"
    val extras = buildCreationExtras { this[key] = value }

    val factory = koinViewModelFactory<TestViewModelWithExtras>(
      scope = koin.scopeRegistry.rootScope,
    )
    val viewModel = factory.create(extras)

    assertNotNull(viewModel)
    assertSame(expected = extras, actual = viewModel.extras)
    assertEquals(expected = value, actual = viewModel.extras[key])
  }
}
