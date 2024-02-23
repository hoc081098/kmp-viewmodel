package com.hoc081098.kmp.viewmodel.koject

import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.createSavedStateHandle
import com.moriatsushi.koject.ExperimentalKojectApi
import com.moriatsushi.koject.component.ComponentExtras

// Copied from https://github.com/mori-atsushi/koject/blob/581b568260645db798d5e2c64d8bdbf305430ae4/android/koject-android-viewmodel/src/main/kotlin/com/moriatsushi/koject/android/viewmodel/ViewModelComponentExtras.kt
@OptIn(ExperimentalKojectApi::class)
internal class ViewModelComponentExtras(
  private val extras: CreationExtras,
) : ComponentExtras<ViewModelComponent> {
  val savedStateHandle: SavedStateHandle
    get() = extras.createSavedStateHandle()
}
