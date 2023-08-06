package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.remember
import com.hoc081098.kmp.viewmodel.ViewModel

@Composable
public actual inline fun <reified VM : ViewModel> kmpViewModel(
  key: String?,
  factory: ViewModelFactory<VM>,
): VM = remember(key, factory) { CompositionViewModel(factory.create()) }.viewModel

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
