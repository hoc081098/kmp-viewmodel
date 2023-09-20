package com.hoc081098.common.navigation.internal

import android.os.Bundle
import android.os.Parcelable
import com.hoc081098.common.navigation.Route
import com.hoc081098.common.navigation.RouteContent
import com.hoc081098.kmp.viewmodel.InternalKmpViewModelApi
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModelStore

@OptIn(InternalKmpViewModelApi::class)
internal actual fun createViewModelStore(): ViewModelStore =
  ViewModelStore(androidx.lifecycle.ViewModelStore())

internal actual fun createSavedStateHandle(id: String, globalSavedStateHandle: SavedStateHandle): SavedStateHandle {
  val restoredBundle = globalSavedStateHandle.get<Bundle>(id)

  return SavedStateHandle
    .createHandle(restoredBundle, null)
    .also { globalSavedStateHandle.setSavedStateProvider(id, it.savedStateProvider()) }
}

internal const val SAVED_STATE_STACK = "com.hoc081098.common.navigation.stack"

internal actual fun setNavStackSavedStateProvider(
  globalSavedStateHandle: SavedStateHandle,
  savedStateFactory: () -> Map<String, ArrayList<out Any>>
) {
  globalSavedStateHandle.setSavedStateProvider(SAVED_STATE_STACK) {
    Bundle().apply {
      savedStateFactory().forEach { (k, v) ->
        @Suppress("UNCHECKED_CAST")
        when (val first = v[0]) {
          is String -> putStringArrayList(k, v as ArrayList<String>)
          is Parcelable -> putParcelableArrayList(k, v as ArrayList<Parcelable>)
          else -> error("Unknown type: $first")
        }
      }
    }
  }
}

internal actual fun createNavStack(
  globalSavedStateHandle: SavedStateHandle,
  initialRoute: Route,
  contents: List<RouteContent<*>>,
  onStackEntryRemoved: (NavEntry<*>) -> Unit
): NavStack {
  val savedState = globalSavedStateHandle.get<Bundle>(SAVED_STATE_STACK)

  return if (savedState === null) {
    NavStack.create(
      initial = NavEntry.create(
        route = initialRoute,
        contents = contents,
      ),
      onStackEntryRemoved = onStackEntryRemoved,
    )
  } else {
    NavStack.fromSavedState(
      savedState = savedState
        .keySet()
        .associateWith { savedState.get(it) as ArrayList<*> },
      contents = contents,
      onStackEntryRemoved = onStackEntryRemoved,
    )
  }
}

internal actual fun removeSavedStateHandle(
  id: String,
  globalSavedStateHandle: SavedStateHandle
): Any? = globalSavedStateHandle.run {
  clearSavedStateProvider(id)
  remove<Bundle>(id)
}
