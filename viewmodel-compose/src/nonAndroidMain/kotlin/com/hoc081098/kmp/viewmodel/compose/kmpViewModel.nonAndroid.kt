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
  val nonNullKey = rememberViewModelKey(key, kClass)

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
internal fun <VM : ViewModel> rememberViewModelKey(key: String?, kClass: KClass<VM>): String {
  if (key != null) {
    return key
  }

  return remember(kClass) {
    // Copied from androidx.lifecycle.ViewModelProvider.kt

    val canonicalName = kClass.qualifiedName
      ?: throw IllegalArgumentException("Local and anonymous classes can not be ViewModels")

    "$DEFAULT_KEY:$canonicalName"
  }
}

// Copied from androidx.lifecycle.ViewModelProvider.kt
private const val DEFAULT_KEY = "androidx.lifecycle.ViewModelProvider.DefaultKey"

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
