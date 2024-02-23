package com.hoc081098.common.navigation.internal

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.benasher44.uuid.uuid4
import com.hoc081098.common.navigation.Route
import com.hoc081098.common.navigation.RouteContent
import kotlin.jvm.JvmField

/**
 * A navigation entry in the navigation stack.
 */
@Immutable // FIXME: RouteContent and isDestroyed is not immutable
internal class NavEntry<T : Route> private constructor(
  @JvmField @Stable val id: String,
  @JvmField @Stable val route: T,
  @JvmField val content: RouteContent<T>,
) {
  var isDestroyed: Boolean = false
    private set

  internal fun markAsDestroyed() {
    isDestroyed = true
  }

  override fun toString(): String = "NavEntry(id=$id, route=$route, isDestroyed=$isDestroyed)"

  override fun hashCode(): Int {
    var result = id.hashCode()
    result = 31 * result + route.hashCode()
    return result
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || this::class != other::class) return false

    other as NavEntry<*>

    if (id != other.id) return false
    if (route != other.route) return false

    return true
  }

  companion object {
    fun <T : Route> create(
      route: T,
      contents: List<RouteContent<*>>,
      id: String = uuid4().toString(),
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
