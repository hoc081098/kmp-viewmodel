@file:Suppress("ConstPropertyName")

package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.hoc081098.kmp.viewmodel.ViewModel
import kotlin.reflect.KClass

internal expect val <T : Any> KClass<T>.canonicalName: String?

@PublishedApi
@Composable
internal fun <VM : ViewModel> rememberDefaultViewModelKey(kClass: KClass<VM>): String = remember(kClass) {
  // Copied from androidx.lifecycle.ViewModelProvider.kt

  val canonicalName = kClass.canonicalName
    ?: throw IllegalArgumentException("Local and anonymous classes can not be ViewModels")

  "$DefaultKey:$canonicalName"
}

// Copied from androidx.lifecycle.ViewModelProvider.kt
private const val DefaultKey = "androidx.lifecycle.ViewModelProvider.DefaultKey"
