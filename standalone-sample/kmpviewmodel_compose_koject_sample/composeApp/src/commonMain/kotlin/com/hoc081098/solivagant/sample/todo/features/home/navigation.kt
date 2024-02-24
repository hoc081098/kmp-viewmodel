package com.hoc081098.solivagant.sample.todo.features.home

import com.hoc081098.kmp.viewmodel.parcelable.Parcelize
import com.hoc081098.solivagant.navigation.NavRoot
import com.hoc081098.solivagant.navigation.ScreenDestination
import kotlin.jvm.JvmField

@Parcelize
data object HomeScreenRoute : NavRoot

@JvmField
val HomeScreenDestination = ScreenDestination<HomeScreenRoute> { _, modifier ->
  HomeScreen(modifier = modifier)
}
