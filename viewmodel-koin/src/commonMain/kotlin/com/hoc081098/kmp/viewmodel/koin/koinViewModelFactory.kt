package com.hoc081098.kmp.viewmodel.koin

import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.InternalKmpViewModelApi
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelFactory
import com.hoc081098.kmp.viewmodel.createSavedStateHandle
import com.hoc081098.kmp.viewmodel.viewModelFactory
import kotlin.reflect.KClass
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.ParametersHolder
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope

@OptIn(InternalKmpViewModelApi::class)
public inline fun <reified VM : ViewModel> koinViewModelFactory(
  scope: Scope,
  qualifier: Qualifier? = null,
  noinline parameters: ParametersDefinition? = null,
): ViewModelFactory<VM> = viewModelFactory {
  scope.get<VM>(
    qualifier = qualifier,
    parameters = { KmpViewModelParametersHolder(parameters, this) },
  )
}

// Copied from https://github.com/InsertKoinIO/koin/blob/e1f58a69d762d26c485b2ca7b7200e621c8ee6c0/android/koin-android/src/main/java/org/koin/androidx/viewmodel/parameter/AndroidParametersHolder.kt#L10
@PublishedApi
@InternalKmpViewModelApi
internal class KmpViewModelParametersHolder(
  initialValues: ParametersDefinition? = null,
  private val extras: CreationExtras,
) : ParametersHolder(
  initialValues?.invoke()?.values?.toMutableList()
    ?: mutableListOf(),
) {

  override fun <T> elementAt(i: Int, clazz: KClass<*>): T {
    return createSavedStateHandleOrElse(clazz) { super.elementAt(i, clazz) }
  }

  override fun <T> getOrNull(clazz: KClass<*>): T? {
    return createSavedStateHandleOrElse(clazz) { super.getOrNull(clazz) }
  }

  private inline fun <T> createSavedStateHandleOrElse(clazz: KClass<*>, block: () -> T): T {
    return if (clazz == SavedStateHandle::class) {
      @Suppress("UNCHECKED_CAST")
      extras.createSavedStateHandle() as T
    } else {
      block()
    }
  }
}
