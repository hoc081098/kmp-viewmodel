package com.hoc081098.kmp.viewmodel

import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass

public fun <VM : ViewModel> ViewModelFactory<VM>.toAndroidX(): ViewModelProvider.Factory =
  object : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
      @Suppress("UNCHECKED_CAST")
      return this@toAndroidX.create(extras) as T
    }
  }
