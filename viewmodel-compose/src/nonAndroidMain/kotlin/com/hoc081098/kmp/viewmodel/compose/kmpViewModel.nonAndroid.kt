package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.remember
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.VIEW_MODEL_KEY
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelFactory
import com.hoc081098.kmp.viewmodel.buildCreationExtras
import kotlin.reflect.KClass

@Composable
public actual inline fun <reified VM : ViewModel> kmpViewModel(
  key: String?,
  extras: CreationExtras,
  factory: ViewModelFactory<VM>,
): VM {
  val kClass = VM::class
  val nonNullKey = key ?: rememberViewModelKey(kClass)

  return remember(nonNullKey, kClass, factory, extras) {
    CompositionViewModel(
      viewModel = factory.create(
        buildCreationExtras(extras) {
          this[VIEW_MODEL_KEY] = nonNullKey
        },
      ),
    )
  }.viewModel
}

@PublishedApi
@Composable
internal fun <VM : ViewModel> rememberViewModelKey(kClass: KClass<VM>): String = remember(kClass) {
  // Copied from androidx.lifecycle.ViewModelProvider.kt

  val canonicalName = kClass.qualifiedName
    ?: throw IllegalArgumentException("Local and anonymous classes can not be ViewModels")

  "$DefaultKey:$canonicalName"
}

// Copied from androidx.lifecycle.ViewModelProvider.kt
private const val DefaultKey = "androidx.lifecycle.ViewModelProvider.DefaultKey"

@PublishedApi
internal class CompositionViewModel<VM : ViewModel>(
  val viewModel: VM,
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
