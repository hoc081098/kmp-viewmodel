package com.hoc08198.common.navigation.internal

import com.benasher44.uuid.Uuid
import com.hoc08198.common.navigation.Route
import com.hoc08198.common.navigation.RouteContent

internal class NavEntry<T : Route>(
  val id: String,
  val route: T,
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
