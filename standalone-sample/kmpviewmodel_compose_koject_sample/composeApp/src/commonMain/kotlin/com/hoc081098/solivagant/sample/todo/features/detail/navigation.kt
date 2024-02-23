package com.hoc081098.solivagant.sample.todo.features.detail

import com.hoc081098.kmp.viewmodel.parcelable.Parcelize
import com.hoc081098.solivagant.navigation.NavRoute
import com.hoc081098.solivagant.navigation.ScreenDestination
import kotlin.jvm.JvmField

@Parcelize
data class DetailScreenRoute(
  val id: String,
) : NavRoute

@JvmField
val DetailScreenDestination = ScreenDestination<DetailScreenRoute> { route, modifier ->
  DetailScreen(modifier = modifier, route = route)
}
