package com.hoc081098.kmp.viewmodel

/**
 * Creates `SavedStateHandle` that can be used in your ViewModels.
 *
 * ### Other platforms
 *
 * This function simply returns an empty [SavedStateHandle].
 *
 * ### On Android
 *
 * This function requires `enableSavedStateHandles` call during the component
 * initialization. Latest versions of androidx components like `ComponentActivity`, `Fragment`,
 * `NavBackStackEntry` makes this call automatically.
 *
 * This [CreationExtras] must contain `SAVED_STATE_REGISTRY_OWNER_KEY`,
 * `VIEW_MODEL_STORE_OWNER_KEY` and [VIEW_MODEL_KEY].
 *
 * @throws IllegalArgumentException if this `CreationExtras` are missing required keys:
 * `ViewModelStoreOwnerKey`, `SavedStateRegistryOwnerKey`, `VIEW_MODEL_KEY`.
 *
 */
@MainThread
public expect fun CreationExtras.createSavedStateHandle(): SavedStateHandle
