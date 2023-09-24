package com.hoc081098.common.navigation.internal

import com.hoc081098.common.navigation.Route
import com.hoc081098.common.navigation.RouteContent
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.SavedStateHandleFactory
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelStore
import com.hoc081098.kmp.viewmodel.ViewModelStoreOwner

internal const val EXTRA_ROUTE: String = "com.hoc081098.common.navigation.ROUTE"

//region Expect and actual functions to create ViewModelStore, SavedStateHandle, NavStack
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
  onStackEntryRemoved: (NavEntry<*>) -> Unit,
): NavStack
//endregion

private class NavEntryViewModelStoreOwner(override val viewModelStore: ViewModelStore) : ViewModelStoreOwner

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
      NavEntryViewModelStoreOwner(
        viewModelStore = createViewModelStore(),
      )
    }

  //region provideSavedStateHandleFactory
  internal infix fun provideSavedStateHandleFactory(navEntry: NavEntry<*>): SavedStateHandleFactory =
    savedStateHandleFactories.getOrPut(navEntry.id) {
      SavedStateHandleFactory { provideSavedStateHandle(navEntry) }
    }

  private fun provideSavedStateHandle(navEntry: NavEntry<*>): SavedStateHandle = savedStateHandles.getOrPut(navEntry.id) {
    createSavedStateHandle(
      id = navEntry.id,
      globalSavedStateHandle = globalSavedStateHandle,
    ).apply {
      this[EXTRA_ROUTE] = navEntry.route
    }
  }
  //endregion

  internal fun removeEntry(id: String) {
    val store = viewModelStoreOwners.remove(id)
    store?.viewModelStore?.clear()

    savedStateHandles.remove(id)
    removeSavedStateHandle(id, globalSavedStateHandle).also {
      println("$this removeSavedStateHandle $id -> $it")
    }

    savedStateHandleFactories.remove(id)
  }

  //region Save and restore NavStack
  internal fun setNavStackSavedStateProvider(navigator: DefaultNavigator) =
    setNavStackSavedStateProvider(globalSavedStateHandle, navigator::saveState)

  internal fun createNavStack(
    initialRoute: Route,
    contents: List<RouteContent<*>>,
    onStackEntryRemoved: (NavEntry<*>) -> Unit,
  ): NavStack = createNavStack(
    globalSavedStateHandle = globalSavedStateHandle,
    initialRoute = initialRoute,
    contents = contents,
    onStackEntryRemoved = onStackEntryRemoved,
  )
  //endregion
}
