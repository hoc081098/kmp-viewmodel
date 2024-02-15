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

/**
 * Create a [ViewModelFactory] for the given [VM] type,
 * which creates the [VM] by getting it from the given Koin [scope]
 * with the given [qualifier] and [parameters].
 *
 * [SavedStateHandle] will be created and passed to the constructor of [VM] if it's requested.
 * [CreationExtras] passed to [ViewModelFactory.create] will be passed to the constructor of [VM] if it's requested.
 *
 * ### Example
 * ```kotlin
 * class MyRepository
 *
 * class MyViewModel(
 *   val myRepository: MyRepository,
 *   val savedStateHandle: SavedStateHandle,
 *   val id: Int,
 * ) : ViewModel()
 *
 * val myModule = module {
 *   factoryOf(::MyRepository)
 *   factoryOf(::MyViewModel)
 * }
 *
 * val factory = koinViewModelFactory<MyViewModel>(
 *   scope = KoinPlatformTools.defaultContext().get().scopeRegistry.rootScope,
 *   parameters = { parametersOf(1) },
 * )
 * ```
 *
 * @param VM The type of the [ViewModel] to create.
 * @param scope The Koin [Scope] to get the [ViewModel] from.
 * @param qualifier The Koin [Qualifier] to get the [ViewModel] with.
 * @param parameters The Koin [ParametersDefinition] to get the [ViewModel] with.
 *
 * @see viewModelFactory
 * @see [Scope.get]
 */
public inline fun <reified VM : ViewModel> koinViewModelFactory(
  scope: Scope,
  qualifier: Qualifier? = null,
  noinline parameters: ParametersDefinition? = null,
): ViewModelFactory<VM> =
  koinViewModelFactory(
    viewModelClass = VM::class,
    scope = scope,
    qualifier = qualifier,
    parameters = parameters,
  )

/**
 * Create a [ViewModelFactory] for the given [viewModelClass],
 * which creates the [VM] by getting it from the given Koin [scope]
 * with the given [qualifier] and [parameters].
 *
 * [SavedStateHandle] will be created and passed to the constructor of [VM] if it's requested.
 * [CreationExtras] passed to [ViewModelFactory.create] will be passed to the constructor of [VM] if it's requested.
 *
 * ### Example
 * ```kotlin
 * class MyRepository
 *
 * class MyViewModel(
 *   val myRepository: MyRepository,
 *   val savedStateHandle: SavedStateHandle,
 *   val id: Int,
 * ) : ViewModel()
 *
 * val myModule = module {
 *   factoryOf(::MyRepository)
 *   factoryOf(::MyViewModel)
 * }
 *
 * val factory = koinViewModelFactory(
 *   viewModelClass = MyViewModel::class,
 *   scope = KoinPlatformTools.defaultContext().get().scopeRegistry.rootScope,
 *   parameters = { parametersOf(1) },
 * )
 * ```
 *
 * @param viewModelClass The [KClass] of the [ViewModel] to create.
 * @param scope The Koin [Scope] to get the [ViewModel] from.
 * @param qualifier The Koin [Qualifier] to get the [ViewModel] with.
 * @param parameters The Koin [ParametersDefinition] to get the [ViewModel] with.
 *
 * @see viewModelFactory
 * @see [Scope.get]
 */
@OptIn(InternalKmpViewModelApi::class)
public fun <VM : ViewModel> koinViewModelFactory(
  viewModelClass: KClass<VM>,
  scope: Scope,
  qualifier: Qualifier? = null,
  parameters: ParametersDefinition? = null,
): ViewModelFactory<VM> = viewModelFactory(viewModelClass = viewModelClass) {
  scope.get(
    clazz = viewModelClass,
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
    return createSavedStateHandleOrCreationExtrasOrElse(clazz) { super.elementAt(i, clazz) }
  }

  override fun <T> getOrNull(clazz: KClass<*>): T? {
    return createSavedStateHandleOrCreationExtrasOrElse(clazz) { super.getOrNull(clazz) }
  }

  private inline fun <T> createSavedStateHandleOrCreationExtrasOrElse(clazz: KClass<*>, block: () -> T): T {
    return when (clazz) {
      SavedStateHandle::class -> {
        @Suppress("UNCHECKED_CAST")
        extras.createSavedStateHandle() as T
      }

      CreationExtras::class -> {
        @Suppress("UNCHECKED_CAST")
        extras as T
      }

      else -> block()
    }
  }
}
