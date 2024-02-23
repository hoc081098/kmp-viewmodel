package com.hoc081098.solivagant.sample.todo.features.edit

import com.hoc081098.kmp.viewmodel.parcelable.Parcelize
import com.hoc081098.solivagant.navigation.NavRoute
import com.hoc081098.solivagant.navigation.ScreenDestination
import kotlin.jvm.JvmField

@Parcelize
data class EditScreenRoute(
  val id: String,
) : NavRoute

@JvmField
val EditScreenDestination = ScreenDestination<EditScreenRoute> { route, modifier ->
  EditScreen(modifier = modifier, route = route)
}
