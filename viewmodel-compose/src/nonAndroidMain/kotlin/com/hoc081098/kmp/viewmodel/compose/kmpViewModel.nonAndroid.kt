package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.remember
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.EmptyCreationExtras
import com.hoc081098.kmp.viewmodel.VIEW_MODEL_KEY
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelFactory
import com.hoc081098.kmp.viewmodel.buildCreationExtras
import kotlin.jvm.JvmField
import kotlin.reflect.KClass

@Composable
public actual inline fun <reified VM : ViewModel> kmpViewModel(
  key: String?,
  extras: CreationExtras,
  factory: ViewModelFactory<VM>,
): VM {
  val kClass = remember { VM::class }
  val nonNullKey = key ?: rememberDefaultViewModelKey(kClass)

  return remember(kClass, nonNullKey, factory, extras) {
    CompositionViewModel(
      viewModel = factory.create(
        buildCreationExtras(extras) {
          this[VIEW_MODEL_KEY] = nonNullKey
        },
      ),
    )
  }.viewModel
}

@ReadOnlyComposable
@Composable
public actual fun defaultCreationExtras(): CreationExtras = EmptyCreationExtras

@PublishedApi
@Composable
internal fun <VM : ViewModel> rememberDefaultViewModelKey(kClass: KClass<VM>): String = remember(kClass) {
  // Copied from androidx.lifecycle.ViewModelProvider.kt

  val canonicalName = kClass.qualifiedName
    ?: throw IllegalArgumentException("Local and anonymous classes can not be ViewModels")

  "$DefaultKey:$canonicalName"
}

// Copied from androidx.lifecycle.ViewModelProvider.kt
private const val DefaultKey = "androidx.lifecycle.ViewModelProvider.DefaultKey"

@PublishedApi
internal class CompositionViewModel<VM : ViewModel>(
  @JvmField val viewModel: VM,
) : RememberObserver {
  override fun onAbandoned() {
    viewModel.clear()
  }

  override fun onForgotten() {
    viewModel.clear()
  }

  override fun onRemembered() {
    // Do nothing
  }
}
