package com.hoc081098.common.navigation.internal

import com.hoc081098.common.navigation.Route
import com.hoc081098.common.navigation.RouteContent
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModelStore

internal actual fun createViewModelStore(): ViewModelStore = ViewModelStore()

internal actual fun createSavedStateHandle(
  id: String,
  globalSavedStateHandle: SavedStateHandle
): SavedStateHandle {
  val restoredSavedStateHandle = globalSavedStateHandle.get<SavedStateHandle>(id)

  return SavedStateHandle()
    .apply {
      restoredSavedStateHandle
        ?.keys()
        ?.forEach { k -> this[k] = restoredSavedStateHandle.get<Any?>(k) }
    }
    .also { globalSavedStateHandle[id] = it }
}

internal actual fun setNavStackSavedStateProvider(
  globalSavedStateHandle: SavedStateHandle,
  savedStateFactory: () -> Map<String, ArrayList<out Any>>
) {
  // Do nothing
}

internal actual fun createNavStack(
  globalSavedStateHandle: SavedStateHandle,
  initialRoute: Route,
  contents: List<RouteContent<*>>,
  onStackEntryRemoved: (NavEntry<*>) -> Unit
): NavStack = NavStack.create(
  initial = NavEntry.create(
    route = initialRoute,
    contents = contents,
  ),
  onStackEntryRemoved = onStackEntryRemoved,
)
