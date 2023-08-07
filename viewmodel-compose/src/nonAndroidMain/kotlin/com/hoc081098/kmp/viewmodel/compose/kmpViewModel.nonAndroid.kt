package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.remember
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelFactory

@Composable
public actual inline fun <reified VM : ViewModel> kmpViewModel(
  key: String?,
  extras: CreationExtras,
  factory: ViewModelFactory<VM>,
): VM = remember(key, factory, extras) {
  CompositionViewModel(
    viewModel = factory.create(extras),
  )
}.viewModel

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
