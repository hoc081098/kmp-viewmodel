package com.hoc081098.solivagant.sample.todo.features.add

import com.hoc081098.kmp.viewmodel.parcelable.Parcelize
import com.hoc081098.solivagant.navigation.NavRoute
import com.hoc081098.solivagant.navigation.ScreenDestination
import kotlin.jvm.JvmField

@Parcelize
data object AddScreenRoute : NavRoute

@JvmField
val AddScreenDestination = ScreenDestination<AddScreenRoute> { _, modifier ->
  AddScreen(modifier = modifier)
}
