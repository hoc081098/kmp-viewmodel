package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.EmptyCreationExtras
import com.hoc081098.kmp.viewmodel.MainThread
import com.hoc081098.kmp.viewmodel.VIEW_MODEL_KEY
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelFactory
import com.hoc081098.kmp.viewmodel.edit
import kotlin.reflect.KClass

// TODO: Remove when releasing.
private inline fun log(tag: String, message: () -> String) {
  println("[$tag] ${message()}")
}

private const val TAG = "StoreViewModel"

internal data class Id(
  val key: String,
  val kClass: KClass<*>,
  val factory: ViewModelFactory<*>,
)

private object StoreViewModel {
  private val stores = mutableMapOf<Id, ViewModel>()

  @MainThread
  fun provide(id: Id, factory: () -> ViewModel): ViewModel {
    log(TAG) { "provide: id=$id" }

    val vm = stores.getOrPut(id, factory)
    log(TAG) { "provide: id=$id, vm=$vm" }

    return if (vm.isCleared()) {
      log(TAG) { "provide: id=$id, vm=$vm -> isCleared" }

      // If the ViewModel is cleared, we will create a new one, and replace the old one.
      factory().also {
        log(TAG) { "provide: id=$id -> replace $vm with $it" }

        stores[id] = it
      }
    } else {
      log(TAG) { "provide: id=$id, vm=$vm -> return" }

      vm
    }
  }

  @MainThread
  fun remove(id: Id, existing: ViewModel) {
    log(TAG) { "remove: id=$id, existing=$existing" }

    val removed = stores.remove(id)
    log(TAG) { "remove: id=$id, existing=$existing -> removed=$removed" }

    if (removed === existing) {
      log(TAG) { "remove: id=$id, existing=$existing -> removed === existing ~> clear" }

      existing.clear()
    } else {
      if (removed != null) {
        log(TAG) { "remove: id=$id, existing=$existing -> removed != null ~> error" }
        error("Removed ViewModel $removed does not match the existing ViewModel $existing")
      } else {
        log(TAG) { "remove: id=$id, existing=$existing -> removed == null ~> ignore" }
        // stores does not contains the ViewModel, so ignore.
      }
    }
  }
}

@Composable
public actual inline fun <reified VM : ViewModel> kmpViewModel(
  key: String?,
  extras: CreationExtras,
  clearViewModelRegistry: ClearViewModelRegistry?,
  factory: ViewModelFactory<VM>,
): VM {
  val kClass = remember { VM::class }

  return resolveViewModel(
    id = rememberId(
      key = key ?: rememberDefaultViewModelKey(kClass),
      kClass = kClass,
      factory = factory,
    ),
    extras = extras,
    clearViewModelRegistry = clearViewModelRegistry,
    factory = factory,
  )
}

@PublishedApi
@Composable
internal fun <VM : ViewModel> rememberId(
  key: String,
  kClass: KClass<VM>,
  factory: ViewModelFactory<VM>,
): Id = remember(key, kClass) { Id(key, kClass, factory) }

@Suppress("UNCHECKED_CAST")
@Composable
@PublishedApi
internal fun <VM : ViewModel> resolveViewModel(
  id: Id,
  extras: CreationExtras,
  clearViewModelRegistry: ClearViewModelRegistry?,
  factory: ViewModelFactory<VM>,
): VM {
  val vm = remember(id) {
    log(TAG) { "resolveViewModel: id=$id, extras=$extras, clearViewModelRegistry=$clearViewModelRegistry" }

    StoreViewModel.provide(
      id = id,
      factory = {
        factory.create(
          extras.edit {
            this[VIEW_MODEL_KEY] = id.key
          },
        )
      },
    ) as VM
  }

  if (clearViewModelRegistry == null) {
    // if clearViewModelRegistry is null, we tie the lifetime of the ViewModel to the lifetime of this composable.

    DisposableEffect(id, vm) {
      log(TAG) { "DisposableEffect: id=$id, vm=$vm" }

      onDispose { StoreViewModel.remove(id, vm) }
    }
  } else {
    // Otherwise, we will clear the ViewModel when the clearViewModelRegistry triggers.

    DisposableEffect(id, vm, clearViewModelRegistry) {
      log(TAG) { "DisposableEffect: id=$id, vm=$vm, clearViewModelRegistry=$clearViewModelRegistry" }

      clearViewModelRegistry.register(listOf(id, vm)) {
        log(TAG) { "DisposableEffect: id=$id, vm=$vm, clearViewModelRegistry=$clearViewModelRegistry -> invoke" }

        return@register { StoreViewModel.remove(id, vm) }
      }
      onDispose {}
    }
  }

  return vm
}

@Stable
public actual fun defaultCreationExtras(): CreationExtras = EmptyCreationExtras
