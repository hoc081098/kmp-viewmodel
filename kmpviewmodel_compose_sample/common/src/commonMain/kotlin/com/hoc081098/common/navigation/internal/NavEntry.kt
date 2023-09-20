package com.hoc081098.common.navigation.internal

import androidx.compose.runtime.Stable
import com.benasher44.uuid.Uuid
import com.hoc081098.common.navigation.Route
import com.hoc081098.common.navigation.RouteContent

internal class NavEntry<T : Route>(
  @Stable val id: String,
  @Stable val route: T,
  val content: RouteContent<T>
) {
  companion object {
    fun <T : Route> create(
      route: T,
      contents: List<RouteContent<*>>,
    ): NavEntry<T> {
      val contentForRoute = contents
        .first { it.id.type == route::class } as RouteContent<T>

      return NavEntry(
        id = Uuid.randomUUID().toString(),
        route = route,
        content = contentForRoute,
      )
    }
  }
}
