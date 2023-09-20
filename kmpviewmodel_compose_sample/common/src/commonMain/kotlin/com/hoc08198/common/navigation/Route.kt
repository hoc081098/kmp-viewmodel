package com.hoc08198.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.hoc081098.kmp.viewmodel.parcelable.Parcelable
import kotlin.reflect.KClass

@Immutable
interface Route : Parcelable

interface Navigator {
  fun navigateTo(screen: Route)
  fun navigateBack()
}

interface RouteContent<T : Route> {
  @Immutable
  @JvmInline
  value class Id<T : Route>(val type: KClass<T>)

  @Stable
  val id: Id<T>

  @Composable
  fun Content(route: T)
}

inline fun <reified T : Route> routeContent(
  noinline content: @Composable (route: T) -> Unit
): RouteContent<T> =
  object : RouteContent<T> {
    override val id: RouteContent.Id<T> = RouteContent.Id(T::class)

    @Composable
    override fun Content(route: T) = content(route)
  }
