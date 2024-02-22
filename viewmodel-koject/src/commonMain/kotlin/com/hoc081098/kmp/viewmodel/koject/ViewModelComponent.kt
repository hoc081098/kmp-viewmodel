package com.hoc081098.kmp.viewmodel.koject

import com.moriatsushi.koject.ExperimentalKojectApi
import com.moriatsushi.koject.Provides
import com.moriatsushi.koject.component.Component

// Copied from https://github.com/mori-atsushi/koject/blob/581b568260645db798d5e2c64d8bdbf305430ae4/android/koject-android-viewmodel/src/main/kotlin/com/moriatsushi/koject/android/viewmodel/ViewModelComponent.kt
/**
 * Can be provided as [com.hoc081098.kmp.viewmodel.ViewModel]s or types used by ViewModels
 * when used with @[Provides].
 *
 * ```
 * @Provides
 * @ViewModelComponent
 * class SampleViewModel(
 *     userRepository: UserRepository
 * ): ViewModel()
 * ```
 *
 * Can inject a [com.hoc081098.kmp.viewmodel.SavedStateHandle] using ViewModelComponent.
 *
 * ```
 * @Provides
 * @ViewModelComponent
 * class SavedStateViewModel(
 *     private val savedStateHandle: SavedStateHandle
 * ) : ViewModel()
 * ```
 *
 * Additional available types:
 * * [com.hoc081098.kmp.viewmodel.SavedStateHandle]
 */
@OptIn(ExperimentalKojectApi::class)
@Component
@Retention(AnnotationRetention.BINARY)
@MustBeDocumented
public annotation class ViewModelComponent
