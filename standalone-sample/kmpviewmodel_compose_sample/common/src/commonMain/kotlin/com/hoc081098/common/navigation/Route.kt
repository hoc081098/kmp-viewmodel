package com.hoc081098.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.hoc081098.kmp.viewmodel.InternalKmpViewModelApi
import com.hoc081098.kmp.viewmodel.compose.internal.kClassOf
import com.hoc081098.kmp.viewmodel.parcelable.Parcelable
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.jvm.JvmInline
import kotlin.native.HiddenFromObjC
import kotlin.reflect.KClass

/**
 * Represents the route to a destination.
 *
 * The instance of this will be put into the navigation arguments as a [Parcelable] and is then
 * available to the target screens.
 */
@Immutable
interface Route : Parcelable

interface Navigator {
  fun navigateTo(screen: Route)
  fun navigateBack()
}

/**
 * Represents the content of a route.
 * It is used to map a route to a composable.
 */
interface RouteContent<T : Route> {
  @Immutable
  @JvmInline
  value class Id<T : Route>(val type: KClass<T>)

  /**
   * The id of this content.
   * Basically it is the [KClass] of the route.
   */
  @Stable
  val id: Id<T>

  /**
   * The composable content of this route.
   */
  @Composable
  fun Content(route: T)
}

@OptIn(ExperimentalObjCRefinement::class, InternalKmpViewModelApi::class)
@HiddenFromObjC
inline fun <reified T : Route> routeContent(
  noinline content: @Composable (route: T) -> Unit,
): RouteContent<T> {
  val id = RouteContent.Id(kClassOf<T>())

  return object : RouteContent<T> {
    override val id: RouteContent.Id<T> get() = id

    @Composable
    override fun Content(route: T) = content(route)
  }
}
