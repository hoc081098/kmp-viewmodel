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
