package com.hoc081098.kmp.viewmodel.compose

import androidx.annotation.MainThread
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.viewmodel.CreationExtras.Empty
import androidx.lifecycle.viewmodel.MutableCreationExtras as AndroidXMutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.SAVED_STATE_HANDLE_FACTORY_KEY
import com.hoc081098.kmp.viewmodel.SavedStateHandleFactory
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelFactory
import com.hoc081098.kmp.viewmodel.ViewModelStoreOwner
import com.hoc081098.kmp.viewmodel.edit
import com.hoc081098.kmp.viewmodel.toAndroidX

/**
 * A sentinel [CreationExtras] used to determine
 * whether the user wants to use the default [CreationExtras] defined by the platform or not.
 */
@JvmSynthetic
@JvmField
internal val DefaultCreationExtrasForAndroid: CreationExtras = AndroidXMutableCreationExtras()

@Suppress("NOTHING_TO_INLINE")
@MainThread
@Composable
public actual inline fun <VM : ViewModel> kmpViewModel(
  factory: ViewModelFactory<VM>,
  viewModelStoreOwner: ViewModelStoreOwner,
  key: String?,
  extras: CreationExtras,
  savedStateHandleFactory: SavedStateHandleFactory?,
): VM = resolveViewModel(
  viewModelStoreOwner = viewModelStoreOwner,
  key = key,
  factory = factory,
  extras = extras,
  savedStateHandleFactory = savedStateHandleFactory,
)

@Suppress("LongParameterList")
@JvmSynthetic
@MainThread
@PublishedApi
@Composable
internal fun <VM : ViewModel> resolveViewModel(
  viewModelStoreOwner: ViewModelStoreOwner,
  key: String?,
  factory: ViewModelFactory<VM>,
  extras: CreationExtras,
  savedStateHandleFactory: SavedStateHandleFactory?,
): VM {
  val androidXOwner = remember(viewModelStoreOwner, viewModelStoreOwner::toAndroidX)

  return viewModel(
    modelClass = factory.viewModelClass.java,
    viewModelStoreOwner = androidXOwner,
    key = key,
    factory = remember(factory, factory::toAndroidX),
    extras = if (extras === DefaultCreationExtrasForAndroid) {
      if (androidXOwner is HasDefaultViewModelProviderFactory) {
        androidXOwner.defaultViewModelCreationExtras
      } else {
        Empty
      }
    } else {
      extras
    }.setSavedStateHandleFactory(savedStateHandleFactory),
  )
}

@Suppress("NOTHING_TO_INLINE")
private inline fun CreationExtras.setSavedStateHandleFactory(savedStateHandleFactory: SavedStateHandleFactory?) =
  if (savedStateHandleFactory != null) {
    edit { this[SAVED_STATE_HANDLE_FACTORY_KEY] = savedStateHandleFactory }
  } else {
    this
  }

/**
 * Returns a sentinel `DefaultCreationExtrasForAndroid` for the current platform (Android).
 * This is used to determine whether the user wants to use the default [CreationExtras] defined by the platform or not.
 *
 * The default [CreationExtras] are provided by the [HasDefaultViewModelProviderFactory.defaultViewModelCreationExtras]
 * if the current [androidx.lifecycle.ViewModelStoreOwner] implements [HasDefaultViewModelProviderFactory],
 * otherwise, it is [Empty].
 *
 * @see HasDefaultViewModelProviderFactory
 */
@Stable
public actual fun defaultPlatformCreationExtras(): CreationExtras = DefaultCreationExtrasForAndroid
