package com.hoc081098.kmpviewmodelsample.android

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.hoc081098.kmpviewmodelsample.product_detail.ProductDetailViewModel
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

internal sealed class Route {
  abstract val routePattern: String
  open fun matches(route: String): Boolean = route == routePattern

  object Start : Route() {
    override val routePattern = "start"
    val route = routePattern
  }

  object Products : Route() {
    override val routePattern = "products"
    val route = routePattern
  }

  object Search : Route() {
    override val routePattern = "search"
    val route = routePattern
  }

  object ProductDetail : Route() {
    val idNavArg = navArgument(name = ProductDetailViewModel.ID_KEY) { type = NavType.IntType }

    override val routePattern = "product_detail/{${idNavArg.name}}"

    fun route(id: Int) = routePattern.replace(
      oldValue = """{${idNavArg.name}}""",
      newValue = id.toString(),
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
