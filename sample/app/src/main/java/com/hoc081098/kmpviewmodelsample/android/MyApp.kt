package com.hoc081098.kmpviewmodelsample.android

import android.app.Application
import com.hoc081098.kmpviewmodelsample.startKoinCommon
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MyApp : Application() {
  override fun onCreate() {
    super.onCreate()

    startKoinCommon {
      androidContext(this@MyApp)
      androidLogger()
    }
  }
}
