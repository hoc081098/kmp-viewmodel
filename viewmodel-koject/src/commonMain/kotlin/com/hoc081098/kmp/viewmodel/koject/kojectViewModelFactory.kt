package com.hoc081098.kmp.viewmodel.koject

import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelFactory
import com.hoc081098.kmp.viewmodel.viewModelFactory
import com.moriatsushi.koject.ExperimentalKojectApi
import com.moriatsushi.koject.Qualifier
import com.moriatsushi.koject.inject
import kotlin.reflect.KClass

// Copied from https://github.com/mori-atsushi/koject/blob/581b568260645db798d5e2c64d8bdbf305430ae4/android/koject-android-viewmodel/src/main/kotlin/com/moriatsushi/koject/android/viewmodel/KojectViewModelFactory.kt

/**
 * Instantiate [ViewModel]s provided by Koject.
 *
 * @param qualifier Qualifier for identification.
 * Specify the instantiation of the annotation with [Qualifier].
 */
@OptIn(ExperimentalKojectApi::class)
public inline fun <reified VM : ViewModel> kojectViewModelFactory(
  qualifier: Any? = null,
): ViewModelFactory<VM> =
  kojectViewModelFactory(VM::class) { inject(qualifier, it) }

@PublishedApi
internal fun <VM : ViewModel> kojectViewModelFactory(
  clazz: KClass<VM>,
  initializer: (ViewModelComponentExtras) -> VM,
): ViewModelFactory<VM> =
  viewModelFactory(viewModelClass = clazz) {
    initializer(ViewModelComponentExtras(this))
  }
