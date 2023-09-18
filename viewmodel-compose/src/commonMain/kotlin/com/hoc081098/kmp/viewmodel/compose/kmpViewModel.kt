package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.MainThread
import com.hoc081098.kmp.viewmodel.SavedStateHandleFactory
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelFactory
import com.hoc081098.kmp.viewmodel.ViewModelStoreOwner
import kotlin.jvm.JvmSynthetic

/**
 * Returns an existing [ViewModel] or creates a new one in the given owner
 * (usually, an Android fragment or an Android activity), defaulting to [defaultViewModelStoreOwner].
 *
 * The created [ViewModel] is associated with the given [viewModelStoreOwner] and will be retained
 * as long as the owner is alive (e.g. if it is an Android activity, until it is finished or process is killed).
 *
 * If default arguments are provided via the [CreationExtras], they will be available to the
 * appropriate [factory] when the [ViewModel] is created.
 *
 * @param factory The [ViewModelFactory] that should be used to create the [ViewModel].
 * @param viewModelStoreOwner The owner of the [ViewModel] that controls the scope and lifetime
 * of the returned [ViewModel]. Defaults to using [defaultViewModelStoreOwner].
 * @param key The key to use to identify the [ViewModel].
 * or null if you would like to use the default factory from the [defaultViewModelStoreOwner]
 * @param extras The default extras used to create the [ViewModel].
 * @return A [ViewModel] that is an instance of the given [VM] type.
 */
@MainThread
@Composable
public expect inline fun <reified VM : ViewModel> kmpViewModel(
  factory: ViewModelFactory<VM>,
  viewModelStoreOwner: ViewModelStoreOwner = defaultViewModelStoreOwner(),
  key: String? = null,
  extras: CreationExtras = defaultPlatformCreationExtras(),
  savedStateHandleFactory: SavedStateHandleFactory? = LocalSavedStateHandleFactory.current,
): VM

/**
 * Returns an existing [ViewModel] or creates a new one in the given owner
 * (usually, an Android fragment or an Android activity), defaulting to [defaultViewModelStoreOwner].
 *
 * The created [ViewModel] is associated with the given [viewModelStoreOwner] and will be retained
 * as long as the owner is alive (e.g. if it is an Android activity, until it is finished or process is killed).
 *
 * If default arguments are provided via the [CreationExtras], they will be available to the
 * appropriate factory when the [ViewModel] is created.
 *
 * @param viewModelStoreOwner The owner of the [ViewModel] that controls the scope and lifetime
 * of the returned [ViewModel]. Defaults to using [defaultViewModelStoreOwner].
 * @param key The key to use to identify the [ViewModel].
 * or null if you would like to use the default factory from the [defaultViewModelStoreOwner]
 * @param extras The default extras used to create the [ViewModel].
 * @param factory A lambda that creates the [ViewModel].
 * It will be remembered as a [ViewModelFactory], that should be used to create the [ViewModel].
 * @return A [ViewModel] that is an instance of the given [VM] type.
 *
 * @see rememberViewModelFactory
 */
@MainThread
@Composable
public inline fun <reified VM : ViewModel> kmpViewModel(
  key: String? = null,
  viewModelStoreOwner: ViewModelStoreOwner = defaultViewModelStoreOwner(),
  extras: CreationExtras = defaultPlatformCreationExtras(),
  savedStateHandleFactory: SavedStateHandleFactory? = LocalSavedStateHandleFactory.current,
  crossinline factory: @DisallowComposableCalls CreationExtras.() -> VM,
): VM = kmpViewModel(
  savedStateHandleFactory = savedStateHandleFactory,
  key = key,
  extras = extras,
  viewModelStoreOwner = viewModelStoreOwner,
  factory = rememberViewModelFactory(factory),
)

/**
 * Returns current composition local value for the [ViewModelStoreOwner] provided by [LocalViewModelStoreOwner].
 * or [defaultPlatformViewModelStoreOwner] if one has not been provided.
 */
@JvmSynthetic
@PublishedApi
@MainThread
@Composable
internal inline fun defaultViewModelStoreOwner(): ViewModelStoreOwner =
  LocalViewModelStoreOwner.current
    ?: defaultPlatformViewModelStoreOwner()
