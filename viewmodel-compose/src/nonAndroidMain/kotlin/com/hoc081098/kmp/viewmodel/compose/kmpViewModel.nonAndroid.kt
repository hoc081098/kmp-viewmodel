package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.EmptyCreationExtras
import com.hoc081098.kmp.viewmodel.MainThread
import com.hoc081098.kmp.viewmodel.SAVED_STATE_HANDLE_FACTORY_KEY
import com.hoc081098.kmp.viewmodel.SavedStateHandleFactory
import com.hoc081098.kmp.viewmodel.VIEW_MODEL_KEY
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelFactory
import com.hoc081098.kmp.viewmodel.ViewModelStoreOwner
import com.hoc081098.kmp.viewmodel.edit
import kotlin.jvm.JvmSynthetic
import kotlin.reflect.KClass

@MainThread
@Composable
public actual inline fun <reified VM : ViewModel> kmpViewModel(
  factory: ViewModelFactory<VM>,
  viewModelStoreOwner: ViewModelStoreOwner,
  key: String?,
  extras: CreationExtras,
  savedStateHandleFactory: SavedStateHandleFactory?,
): VM {
  val kClass = remember { VM::class }
  val nonNullKey = key ?: rememberDefaultViewModelKey(kClass)

  return resolveViewModel(
    key = nonNullKey,
    kClass = kClass,
    factory = factory,
    extras = extras.edit {
      this[VIEW_MODEL_KEY] = nonNullKey
      savedStateHandleFactory?.let { this[SAVED_STATE_HANDLE_FACTORY_KEY] = it }
    },
    viewModelStoreOwner = viewModelStoreOwner,
  )
}

@JvmSynthetic
@MainThread
@PublishedApi
internal fun <VM : ViewModel> resolveViewModel(
  key: String,
  kClass: KClass<VM>,
  extras: CreationExtras,
  factory: ViewModelFactory<VM>,
  viewModelStoreOwner: ViewModelStoreOwner,
): VM = viewModelStoreOwner.getOrCreateViewModel(
  key = key,
  kClass = kClass,
  factory = factory,
  extras = extras,
)

/**
 * Returns [EmptyCreationExtras] for the current platform (non-Android).
 */
@Stable
public actual fun defaultPlatformCreationExtras(): CreationExtras = EmptyCreationExtras

/**
 * Remember a [ViewModelStoreOwner] in the current composition.
 * It clears its [ViewModelStoreOwner.viewModelStore] when the current @Composable leaves the composition.
 * Basically, its scope ties the lifecycle of the current composition.
 */
@MainThread
@Composable
public actual fun defaultPlatformViewModelStoreOwner(): ViewModelStoreOwner = rememberDefaultViewModelStoreOwner()
