package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.EmptyCreationExtras
import com.hoc081098.kmp.viewmodel.InternalKmpViewModelApi
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

// TODO: Remove when releasing.
private const val TAG = "StoreViewModel"

// TODO: Remove when releasing.
internal data class Id(
  val key: String,
  val kClass: KClass<*>,
) {
  override fun toString(): String = "Id{ key='$key', kClass=${kClass.simpleName} }"
}

// TODO: Remove when releasing.
private val Any.debugString: String
  get() = "${this::class.simpleName}@${hashCode()}"

@OptIn(InternalKmpViewModelApi::class)
private object StoreViewModel {
  private val stores = mutableMapOf<Id, ViewModel>()

  @MainThread
  fun provide(id: Id, factory: () -> ViewModel): ViewModel {
    log(TAG) { "provide: START id=$id" }

    val vm = stores.getOrPut(id, factory)

    return if (vm.isCleared()) {
      // If the ViewModel is cleared, we will create a new one, and replace the old one.
      factory().also {
        log(TAG) { "provide: RETURN id=$id -> replace ${vm.debugString} with ${it.debugString}" }

        stores[id] = it
      }
    } else {
      log(TAG) { "provide: RETURN id=$id, vm=${vm.debugString} -> return" }

      vm
    }
  }

  @MainThread
  fun remove(id: Id, existing: ViewModel) {
    log(TAG) { "remove: START id=$id, existing=${existing.debugString}" }

    val removed = stores.remove(id)

    when {
      removed === existing -> {
        log(TAG) { "remove: CLEARED id=$id, existing=${existing.debugString} -> removed === existing ~> clear" }

        existing.clear()
      }

      removed != null -> {
        log(TAG) { "remove: ERROR id=$id, existing=${existing.debugString} -> removed != null ~> error" }

        error("Removed ViewModel $removed does not match the existing $existing")
      }

      else -> {
        log(TAG) { "remove: IGNORED id=$id, existing=${existing.debugString} -> removed == null ~> ignored" }

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
): Id = remember(key, kClass) { Id(key, kClass) }

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
      log(TAG) { "resolveViewModel: DISPOSABLE_EFFECT id=$id, vm=${vm.debugString}" }

      onDispose { StoreViewModel.remove(id, vm) }
    }
  } else {
    // Otherwise, we will clear the ViewModel when the clearViewModelRegistry triggers.

    DisposableEffect(id, vm, clearViewModelRegistry) {
      log(TAG) {
        "resolveViewModel: DISPOSABLE_EFFECT id=$id, vm=${vm.debugString}, " +
          "clearViewModelRegistry=${clearViewModelRegistry.debugString}"
      }

      clearViewModelRegistry.register(listOf(id, vm)) { { StoreViewModel.remove(id, vm) } }

      onDispose {}
    }
  }

  return vm
}

@Stable
public actual fun defaultCreationExtras(): CreationExtras = EmptyCreationExtras
