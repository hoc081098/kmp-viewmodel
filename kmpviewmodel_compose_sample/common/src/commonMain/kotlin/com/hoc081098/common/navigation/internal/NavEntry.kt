package com.hoc081098.common.navigation.internal

import androidx.compose.runtime.Stable
import com.benasher44.uuid.Uuid
import com.hoc081098.common.navigation.Route
import com.hoc081098.common.navigation.RouteContent

/**
 * A navigation entry in the navigation stack.
 */
internal class NavEntry<T : Route> private constructor(
  @JvmField @Stable val id: String,
  @JvmField @Stable val route: T,
  @JvmField val content: RouteContent<T>
) {
  companion object {
    fun <T : Route> create(
      route: T,
      contents: List<RouteContent<*>>,
      id: String = Uuid.randomUUID().toString(),
    ): NavEntry<T> {
      val contentForRoute = contents
        .first { it.id.type == route::class } as RouteContent<T>

      return NavEntry(
        id = id,
        route = route,
        content = contentForRoute,
      )
    }
  }
}
