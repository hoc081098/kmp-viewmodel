package com.hoc081098.kmp.viewmodel.koject

import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelFactory
import com.hoc081098.kmp.viewmodel.viewModelFactory
import com.moriatsushi.koject.ExperimentalKojectApi
import com.moriatsushi.koject.Qualifier
import com.moriatsushi.koject.inject
import kotlin.reflect.KClass

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
