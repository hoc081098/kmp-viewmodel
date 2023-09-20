package com.hoc08198.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.staticCompositionLocalOf
import com.hoc08198.common.navigation.internal.NavEntry
import com.hoc08198.common.navigation.internal.rememberDefaultNavigator

val LocalNavigator = staticCompositionLocalOf<Navigator> {
  error("Can't use Navigator outside of a navigator NavHost")
}

@Composable
fun NavHost(
  initialRoute: Route,
  contents: List<RouteContent<*>>,
) {
  val saveableStateHolder = rememberSaveableStateHolder()

  val navigator = rememberDefaultNavigator(
    initialRoute = initialRoute,
    contents = contents,
    saveableStateHolder = saveableStateHolder,
  )

  val navEntry = navigator.visibleEntryState.value
  CompositionLocalProvider(LocalNavigator provides navigator) {
    saveableStateHolder.SaveableStateProvider(key = navEntry.id) {
      Content(navEntry)
    }
  }
}

@Composable
private fun <T : Route> Content(navEntry: NavEntry<T>) {
  navEntry.content.Content(route = navEntry.route)
}

