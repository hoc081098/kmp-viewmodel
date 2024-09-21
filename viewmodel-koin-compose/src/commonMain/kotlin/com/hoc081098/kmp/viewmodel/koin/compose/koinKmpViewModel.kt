package com.hoc081098.kmp.viewmodel.koin.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.InternalKmpViewModelApi
import com.hoc081098.kmp.viewmodel.MainThread
import com.hoc081098.kmp.viewmodel.SavedStateHandleFactory
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelStoreOwner
import com.hoc081098.kmp.viewmodel.compose.LocalSavedStateHandleFactory
import com.hoc081098.kmp.viewmodel.compose.defaultPlatformCreationExtras
import com.hoc081098.kmp.viewmodel.compose.defaultViewModelStoreOwner
import com.hoc081098.kmp.viewmodel.compose.internal.getCanonicalName
import com.hoc081098.kmp.viewmodel.compose.internal.kClassOf
import com.hoc081098.kmp.viewmodel.compose.kmpViewModel
import com.hoc081098.kmp.viewmodel.koin.koinViewModelFactory
import kotlin.jvm.JvmSynthetic
import kotlin.reflect.KClass
import org.koin.compose.currentKoinScope
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.emptyParametersHolder
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope

@Suppress("LongParameterList")
@OptIn(InternalKmpViewModelApi::class)
@MainThread
@Composable
public fun <VM : ViewModel> koinKmpViewModel(
  viewModelClass: KClass<VM>,
  viewModelStoreOwner: ViewModelStoreOwner = defaultViewModelStoreOwner(),
  key: String? = null,
  extras: CreationExtras = defaultPlatformCreationExtras(),
  savedStateHandleFactory: SavedStateHandleFactory? = LocalSavedStateHandleFactory.current,
  qualifier: Qualifier? = null,
  scope: Scope = currentKoinScope(),
  parameters: ParametersDefinition? = null,
): VM {
  // This will always refer to the latest parameters
  val currentParameters by rememberUpdatedState(parameters)

  val factory = remember(viewModelClass, scope, qualifier) {
    koinViewModelFactory(
      viewModelClass = viewModelClass,
      scope = scope,
      qualifier = qualifier,
      parameters = { currentParameters?.invoke() ?: emptyParametersHolder() },
    )
  }

  val className = getCanonicalName(viewModelClass)
    ?: throw IllegalArgumentException("Local and anonymous classes can not be ViewModels")

  val vmKey = remember(qualifier, className, key) {
    getViewModelKey(
      qualifier = qualifier,
      key = key,
      className = className,
    )
  }

  return kmpViewModel(
    factory = factory,
    viewModelStoreOwner = viewModelStoreOwner,
    key = vmKey,
    extras = extras,
    savedStateHandleFactory = savedStateHandleFactory,
  )
}

@Suppress("LongParameterList")
@OptIn(InternalKmpViewModelApi::class)
@MainThread
@Composable
@NonRestartableComposable
public inline fun <reified VM : ViewModel> koinKmpViewModel(
  viewModelStoreOwner: ViewModelStoreOwner = defaultViewModelStoreOwner(),
  key: String? = null,
  extras: CreationExtras = defaultPlatformCreationExtras(),
  savedStateHandleFactory: SavedStateHandleFactory? = LocalSavedStateHandleFactory.current,
  qualifier: Qualifier? = null,
  scope: Scope = currentKoinScope(),
  noinline parameters: ParametersDefinition? = null,
): VM =
  koinKmpViewModel(
    viewModelClass = kClassOf<VM>(),
    viewModelStoreOwner = viewModelStoreOwner,
    key = key,
    extras = extras,
    savedStateHandleFactory = savedStateHandleFactory,
    qualifier = qualifier,
    scope = scope,
    parameters = parameters,
  )

// Copied from https://github.com/InsertKoinIO/koin/blob/74f91987ef94e63e8ea23ac9ed0ce24d6650d742/projects/android/koin-android/src/main/java/org/koin/androidx/viewmodel/GetViewModel.kt#L49
@InternalKmpViewModelApi
@JvmSynthetic
internal fun getViewModelKey(qualifier: Qualifier?, key: String?, className: String): String? {
  return when {
    key != null -> key
    qualifier != null -> qualifier.value + "_" + className
    else -> null
  }
}
