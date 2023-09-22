package com.hoc081098.common.navigation.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.hoc081098.common.navigation.Navigator
import com.hoc081098.common.navigation.Route
import com.hoc081098.common.navigation.RouteContent

/**
 * A navigation entry in the navigation stack.
 * This is used to change the internal stack.
 */
internal class DefaultNavigator(
  private val stack: NavStack,
  private val contents: List<RouteContent<*>>,
) : Navigator {
  internal val visibleEntryState get() = stack.visibleEntryState
  internal val canNavigateBackState get() = stack.canNavigateBackState
  internal val isPop get() = stack.isPop

  override fun navigateTo(screen: Route) {
    stack.pushWithTransition(
      NavEntry.create(
        route = screen,
        contents = contents,
      )
    )
  }

  override fun navigateBack() {
    stack.popWithTransition()
  }

  internal fun markTransitionComplete() {
    stack.transitionsInProgress
      .value
      .forEach(stack::markTransitionComplete)
  }

  internal fun saveState() = stack.saveState()
}

@Composable
internal fun rememberDefaultNavigator(
  initialRoute: Route,
  contents: List<RouteContent<*>>,
  onStackEntryRemoved: (NavEntry<*>) -> Unit,
  navStoreViewModel: NavStoreViewModel,
): DefaultNavigator = remember(navStoreViewModel) {
  val stack = navStoreViewModel.createNavStack(
    initialRoute = initialRoute,
    contents = contents,
    onStackEntryRemoved = onStackEntryRemoved,
  )

  DefaultNavigator(
    stack = stack,
    contents = contents,
  ).also(navStoreViewModel::setNavStackSavedStateProvider)
}
