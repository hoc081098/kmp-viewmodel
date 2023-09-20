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
  private val viewModelStoreOwners = mutableMapOf<String, DefaultViewModelStoreOwner>()
  private val savedStateHandles = mutableMapOf<String, SavedStateHandle>()
  private val savedStateHandleFactories = mutableMapOf<String, SavedStateHandleFactory>()

  init {
    println("$this init")
    addCloseable { println("$this close") }
  }

  internal infix fun provideViewModelStoreOwner(id: String): ViewModelStoreOwner = viewModelStoreOwners
    .getOrPut(id) {
      DefaultViewModelStoreOwner(
        viewModelStore = createViewModelStore()
      ).also { addCloseable(it.viewModelStore::clear) }
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
