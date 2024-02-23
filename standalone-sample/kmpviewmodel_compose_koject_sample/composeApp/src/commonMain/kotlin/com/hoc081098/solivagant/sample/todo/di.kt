package com.hoc081098.solivagant.sample.todo

import com.hoc081098.solivagant.navigation.NavEventNavigator
import com.moriatsushi.koject.Koject
import com.moriatsushi.koject.KojectBuilder
import com.moriatsushi.koject.Provides
import com.moriatsushi.koject.Singleton
import com.moriatsushi.koject.start

internal object CommonModule {
  @Provides
  @Singleton
  fun navEventNavigator() = NavEventNavigator()
}

fun startKoject(builder: KojectBuilder.() -> Unit = {}) = Koject.start(builder = builder)
