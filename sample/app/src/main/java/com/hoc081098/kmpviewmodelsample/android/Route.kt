package com.hoc081098.kmpviewmodelsample.android

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.hoc081098.kmp.viewmodel.safe.DelicateSafeSavedStateHandleApi
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

  fun matches(route: String): Boolean = route == routePattern

  data object Start : Route() {
    override val routePattern = "start"
    val route = routePattern
  }

  data object Products : Route() {
    override val routePattern = "products"
    val route = routePattern
  }

  data object Search : Route() {
    override val routePattern = "search"
    val route = routePattern
  }

  data object ProductDetail : Route() {
    @OptIn(DelicateSafeSavedStateHandleApi::class)
    val idNavArg = navArgument(name = ProductDetailViewModel.ID_SAVED_KEY.key) { type = NavType.IntType }

    private inline val idNavArgName get() = idNavArg.name

    override val routePattern = "product_detail/{$idNavArgName}"

    fun route(id: Int) = routePattern.replace(
      oldValue = """{$idNavArgName}""",
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
