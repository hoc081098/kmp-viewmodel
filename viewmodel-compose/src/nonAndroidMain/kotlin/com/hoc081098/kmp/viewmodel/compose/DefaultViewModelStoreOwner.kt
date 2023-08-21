package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.remember
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.MainThread
import com.hoc081098.kmp.viewmodel.VIEW_MODEL_KEY
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelFactory
import com.hoc081098.kmp.viewmodel.ViewModelStore
import com.hoc081098.kmp.viewmodel.ViewModelStoreOwner
import com.hoc081098.kmp.viewmodel.edit
import kotlin.LazyThreadSafetyMode.NONE
import kotlin.reflect.KClass

internal class DefaultViewModelStoreOwner : ViewModelStoreOwner, RememberObserver {
  private val viewModelStoreLazy = lazy(NONE) { ViewModelStore() }
  override val viewModelStore: ViewModelStore by viewModelStoreLazy

  override fun onAbandoned() = clearIfInitialized()

  override fun onForgotten() = clearIfInitialized()

  @Suppress("NOTHING_TO_INLINE")
  private inline fun clearIfInitialized() {
    if (viewModelStoreLazy.isInitialized()) {
      viewModelStore.clear()
    }
  }

  override fun onRemembered() {
    // Do nothing
  }
}

@Composable
internal inline fun rememberDefaultViewModelStoreOwner(): ViewModelStoreOwner =
  remember { DefaultViewModelStoreOwner() }

/**
 * Returns an existing ViewModel or creates a new one in the scope of this [ViewModelStoreOwner].
 */
@Suppress("UNCHECKED_CAST")
@MainThread
internal fun <T : ViewModel> ViewModelStoreOwner.getOrCreateViewModel(
  key: String,
  kClass: KClass<T>,
  factory: ViewModelFactory<T>,
  extras: CreationExtras,
): T {
  val viewModel = viewModelStore[key]

  if (viewModel != null) {
    return if (kClass.isInstance(viewModel)) {
      viewModel as T
    } else {
      error("ViewModelStore already contains a ViewModel with the key \"$key\" and a different class: ${viewModel::class}")
    }
  }

  return factory
    .create(
      extras.edit {
        this[VIEW_MODEL_KEY] = key
      },
    )
    .also { viewModelStore.put(key, it) }
}
