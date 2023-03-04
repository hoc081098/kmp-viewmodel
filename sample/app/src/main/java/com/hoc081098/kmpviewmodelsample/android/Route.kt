package com.hoc081098.kmpviewmodelsample.android

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun rememberCurrentRouteAsState(currentBackStackEntryAsState: State<NavBackStackEntry?>): State<Route?> =
  remember(currentBackStackEntryAsState) {
    derivedStateOf {
      currentBackStackEntryAsState.value
        ?.destination
        ?.route
        ?.let(Route::ofOrNull)
    }
  }

internal sealed class Route(
  val routePattern: String,
) {
  open fun matches(route: String): Boolean = route == routePattern

  object Start : Route("start") {
    val route = routePattern
  }

  object Products : Route("products") {
    val route = routePattern
  }

  object Search : Route("search") {
    val route = routePattern
  }

  object ProductDetail : Route("detail/{id}") {
    val idNavArg = navArgument(name = "id") {
      type = NavType.IntType
    }

    fun route(id: Int) = routePattern.replace(
      """{id}""",
      Uri.encode(id.toString())!!,
    )
  }

  companion object {
    private val VALUES: ImmutableList<Route> by lazy {
      persistentListOf(
        Start,
        Products,
        Search,
        ProductDetail,
      )
    }

    fun ofOrNull(route: String): Route? = VALUES.singleOrNull { it.matches(route) }
  }
}
