package com.hoc081098.solivagant.sample.todo

import android.app.Application
import com.moriatsushi.koject.android.application

class AndroidTodoApp : Application() {
  override fun onCreate() {
    super.onCreate()

    startKoject {
      application(this@AndroidTodoApp)
    }
  }
}
