package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.MainThread
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.SavedStateHandleFactory
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelFactory
import com.hoc081098.kmp.viewmodel.ViewModelStoreOwner
import com.hoc081098.kmp.viewmodel.createSavedStateHandle

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
 * Default value is [defaultPlatformCreationExtras].
 * @param savedStateHandleFactory The [SavedStateHandleFactory] that should be used to create the [SavedStateHandle]
 * which can be passed to the [ViewModel] constructor.
 * Default value is provided by [LocalSavedStateHandleFactory].
 * Usually, [createSavedStateHandle] will be used to create the [SavedStateHandle].
 * It will check for the existence of the [SavedStateHandleFactory] in the [CreationExtras].
 * @return A [ViewModel] that is an instance of the given [VM] type.
 *
 * @see defaultViewModelStoreOwner
 * @see createSavedStateHandle
 */
@MainThread
@Composable
public expect inline fun <VM : ViewModel> kmpViewModel(
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
 * appropriate [factory] when the [ViewModel] is created.
 *
 * @param factory The lambda that will be used to create the [ViewModelFactory] (via [rememberViewModelFactory]).
 * @param viewModelStoreOwner The owner of the [ViewModel] that controls the scope and lifetime
 * of the returned [ViewModel]. Defaults to using [defaultViewModelStoreOwner].
 * @param key The key to use to identify the [ViewModel].
 * or null if you would like to use the default factory from the [defaultViewModelStoreOwner]
 * @param extras The default extras used to create the [ViewModel].
 * Default value is [defaultPlatformCreationExtras].
 * @param savedStateHandleFactory The [SavedStateHandleFactory] that should be used to create the [SavedStateHandle]
 * which can be passed to the [ViewModel] constructor.
 * Default value is provided by [LocalSavedStateHandleFactory].
 * Usually, [createSavedStateHandle] will be used to create the [SavedStateHandle].
 * It will check for the existence of the [SavedStateHandleFactory] in the [CreationExtras].
 * @return A [ViewModel] that is an instance of the given [VM] type.
 *
 * @see defaultViewModelStoreOwner
 * @see createSavedStateHandle
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
  factory = rememberViewModelFactory(factory),
  viewModelStoreOwner = viewModelStoreOwner,
  key = key,
  extras = extras,
  savedStateHandleFactory = savedStateHandleFactory,
)
