package com.hoc081098.common.navigation.internal

import com.hoc081098.common.navigation.Route
import com.hoc081098.common.navigation.RouteContent
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.SavedStateHandleFactory
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelStore
import com.hoc081098.kmp.viewmodel.ViewModelStoreOwner

internal class DefaultViewModelStoreOwner(
  override val viewModelStore: ViewModelStore
) : ViewModelStoreOwner

internal expect fun createViewModelStore(): ViewModelStore

internal expect fun createSavedStateHandle(
  id: String,
  globalSavedStateHandle: SavedStateHandle,
): SavedStateHandle

internal expect fun removeSavedStateHandle(
  id: String,
  globalSavedStateHandle: SavedStateHandle,
): Any?

internal expect fun setNavStackSavedStateProvider(
  globalSavedStateHandle: SavedStateHandle,
  savedStateFactory: () -> Map<String, ArrayList<out Any>>,
)

internal expect fun createNavStack(
  globalSavedStateHandle: SavedStateHandle,
  initialRoute: Route,
  contents: List<RouteContent<*>>,
  onStackEntryRemoved: (NavEntry<*>) -> Unit
): NavStack

internal class NavStoreViewModel(
  private val globalSavedStateHandle: SavedStateHandle,
) : ViewModel() {
  private val viewModelStoreOwners = mutableMapOf<String, ViewModelStoreOwner>()
  private val savedStateHandles = mutableMapOf<String, SavedStateHandle>()
  private val savedStateHandleFactories = mutableMapOf<String, SavedStateHandleFactory>()

  init {
    println("$this init")
    addCloseable {
      viewModelStoreOwners.values.forEach {
        println("$this clear ${it.viewModelStore}")
        it.viewModelStore.clear()
      }
      viewModelStoreOwners.clear()

      savedStateHandles.keys.forEach { id ->
        removeSavedStateHandle(id, globalSavedStateHandle)
          .also { println("$this removeSavedStateHandle $id -> $it") }
      }
      savedStateHandles.clear()

      savedStateHandleFactories.clear()

      println("$this closed")
    }
  }

  internal infix fun provideViewModelStoreOwner(id: String): ViewModelStoreOwner = viewModelStoreOwners
    .getOrPut(id) {
      DefaultViewModelStoreOwner(
        viewModelStore = createViewModelStore()
      )
    }

  internal infix fun provideSavedStateHandleFactory(id: String): SavedStateHandleFactory =
    savedStateHandleFactories.getOrPut(id) {
      SavedStateHandleFactory { provideSavedStateHandle(id) }
    }

  private fun provideSavedStateHandle(id: String): SavedStateHandle = savedStateHandles.getOrPut(id) {
    createSavedStateHandle(
      id = id,
      globalSavedStateHandle = globalSavedStateHandle
    )
  }

  internal fun removeEntry(id: String) {
    val store = viewModelStoreOwners.remove(id)
    store?.viewModelStore?.clear()

    savedStateHandles.remove(id)
    removeSavedStateHandle(id, globalSavedStateHandle).also {
      println("$this removeSavedStateHandle $id -> $it")
    }

    savedStateHandleFactories.remove(id)
  }

  internal fun setNavStackSavedStateProvider(navigator: DefaultNavigator) =
    setNavStackSavedStateProvider(globalSavedStateHandle, navigator::saveState)

  internal fun createNavStack(
    initialRoute: Route,
    contents: List<RouteContent<*>>,
    onStackEntryRemoved: (NavEntry<*>) -> Unit
  ): NavStack = createNavStack(
    globalSavedStateHandle = globalSavedStateHandle,
    initialRoute = initialRoute,
    contents = contents,
    onStackEntryRemoved = onStackEntryRemoved,
  )
}
