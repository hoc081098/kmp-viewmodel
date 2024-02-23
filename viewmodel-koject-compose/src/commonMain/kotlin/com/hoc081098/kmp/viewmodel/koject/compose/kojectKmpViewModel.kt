package com.hoc081098.kmp.viewmodel.koject.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.InternalKmpViewModelApi
import com.hoc081098.kmp.viewmodel.MainThread
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.SavedStateHandleFactory
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelStoreOwner
import com.hoc081098.kmp.viewmodel.compose.LocalSavedStateHandleFactory
import com.hoc081098.kmp.viewmodel.compose.defaultPlatformCreationExtras
import com.hoc081098.kmp.viewmodel.compose.defaultViewModelStoreOwner
import com.hoc081098.kmp.viewmodel.compose.kmpViewModel
import com.hoc081098.kmp.viewmodel.koject.kojectViewModelFactory
import kotlin.jvm.JvmSynthetic

/**
 * Returns an existing [ViewModel] or creates a new one by `Koject`.
 *
 * ```
 * @Composable
 * fun TopPage(
 *     viewModel: TopViewModel = kojectKmpViewModel()
 * ) {
 *     /* ... */
 * }
 * ```
 *
 * @param viewModelStoreOwner The owner of the [ViewModel] that controls the scope and lifetime
 * of the returned [ViewModel]. Defaults to using [defaultViewModelStoreOwner].
 * @param key The key to use to identify the [ViewModel].
 * @param extras The default extras used to create the [ViewModel].
 * @param savedStateHandleFactory The factory to create [SavedStateHandle] for the [ViewModel].
 * @param qualifier Qualifier for identification.
 *
 * @see kmpViewModel
 */
@OptIn(InternalKmpViewModelApi::class)
@MainThread
@Composable
@NonRestartableComposable
public inline fun <reified VM : ViewModel> kojectKmpViewModel(
  viewModelStoreOwner: ViewModelStoreOwner = defaultViewModelStoreOwner(),
  key: String? = null,
  extras: CreationExtras = defaultPlatformCreationExtras(),
  savedStateHandleFactory: SavedStateHandleFactory? = LocalSavedStateHandleFactory.current,
  qualifier: Any? = null,
): VM {
  val factory = remember(qualifier) {
    kojectViewModelFactory<VM>(qualifier)
  }

  val vmKey = remember(qualifier, key) {
    getViewModelKey(qualifier, key)
  }

  return kmpViewModel(
    factory = factory,
    viewModelStoreOwner = viewModelStoreOwner,
    key = vmKey,
    extras = extras,
    savedStateHandleFactory = savedStateHandleFactory,
  )
}

// Copied from https://github.com/InsertKoinIO/koin/blob/e1f58a69d762d26c485b2ca7b7200e621c8ee6c0/android/koin-android/src/main/java/org/koin/androidx/viewmodel/GetViewModel.kt#L28
@InternalKmpViewModelApi
@JvmSynthetic
@PublishedApi
internal fun getViewModelKey(qualifier: Any?, key: String?): String? {
  return if (qualifier == null && key == null) {
    null
  } else {
    val q = qualifier?.toString() ?: ""
    val k = key ?: ""
    "$q$k"
  }
}
