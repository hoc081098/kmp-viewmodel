package com.hoc081098.android

import android.app.Application
import com.hoc081098.common.startKoinCommon
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class KmpViewModelComposeKoinApp : Application() {
  override fun onCreate() {
    super.onCreate()

    startKoinCommon {
      androidContext(this@KmpViewModelComposeKoinApp)
      androidLogger(
        level = if (BuildConfig.DEBUG) {
          Level.DEBUG
        } else {
          Level.NONE
        },
      )
      modules(
        MainActivityViewModelModule,
      )
    }
  }
}
