package com.hoc081098.kmp.viewmodel

import kotlin.jvm.JvmField

/**
 * Creates [SavedStateHandle] that can be used in your [ViewModel]s.
 *
 * ### On all platforms
 *
 * If this [CreationExtras] contains [SAVED_STATE_HANDLE_FACTORY_KEY],
 * the returned [SavedStateHandle] will be created by this factory.
 *
 * Basically, [SAVED_STATE_HANDLE_FACTORY_KEY] has higher priority
 * than the default creation logic on each platform.
 *
 * ### Otherwise
 *
 * #### Other platforms
 *
 * This function simply returns an empty [SavedStateHandle].
 *
 * #### On Android
 *
 * This function requires `enableSavedStateHandles` call during the component
 * initialization. Latest versions of androidx components like `ComponentActivity`, `Fragment`,
 * `NavBackStackEntry` makes this call automatically.
 *
 * This [CreationExtras] must contain `SAVED_STATE_REGISTRY_OWNER_KEY`,
 * `VIEW_MODEL_STORE_OWNER_KEY` and [VIEW_MODEL_KEY].
 *
 * @throws IllegalArgumentException on Android, if this `CreationExtras` are missing required keys:
 * `ViewModelStoreOwnerKey`, `SavedStateRegistryOwnerKey`, `VIEW_MODEL_KEY`.
 *
 */
@MainThread
public expect fun CreationExtras.createSavedStateHandle(): SavedStateHandle

/**
 * A key for [SavedStateHandle] that should be passed to [SavedStateHandle] if needed.
 */
@JvmField
public val SAVED_STATE_HANDLE_FACTORY_KEY: CreationExtrasKey<SavedStateHandleFactory> =
  object : CreationExtrasKey<SavedStateHandleFactory> {
    override fun toString(): String = "CreationExtrasKey<SavedStateHandleFactory>"
  }
