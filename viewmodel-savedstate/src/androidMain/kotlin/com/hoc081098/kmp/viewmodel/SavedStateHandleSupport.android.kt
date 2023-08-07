package com.hoc081098.kmp.viewmodel

import androidx.lifecycle.SAVED_STATE_REGISTRY_OWNER_KEY
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.VIEW_MODEL_KEY
import androidx.lifecycle.createSavedStateHandle as androidxCreateSavedStateHandle
import androidx.lifecycle.enableSavedStateHandles

/**
 * Creates `SavedStateHandle` that can be used in your ViewModels
 *
 * This function requires [enableSavedStateHandles] call during the component
 * initialization. Latest versions of androidx components like `ComponentActivity`, `Fragment`,
 * `NavBackStackEntry` makes this call automatically.
 *
 * This [CreationExtras] must contain [SAVED_STATE_REGISTRY_OWNER_KEY],
 * [VIEW_MODEL_STORE_OWNER_KEY] and [VIEW_MODEL_KEY].
 *
 * @throws IllegalArgumentException if this `CreationExtras` are missing required keys:
 * `ViewModelStoreOwnerKey`, `SavedStateRegistryOwnerKey`, `VIEW_MODEL_KEY`
 */
@MainThread
public actual fun CreationExtras.createSavedStateHandle(): SavedStateHandle =
  androidxCreateSavedStateHandle()
