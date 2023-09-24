@file:Suppress("standard:no-empty-file")
// TODO: Use reified type parameter when KT-57727 is fixed
// TODO: Issue https://github.com/JetBrains/compose-multiplatform/issues/3147
// TODO: Issue https://youtrack.jetbrains.com/issue/KT-57727

package com.hoc081098.kmp.viewmodel.compose

// /**
// * Remember a [ViewModelFactory] that will be used to create a [ViewModel].
// *
// * @see remember
// * @see viewModelFactory
// */
// @Composable
// public inline fun <VM : ViewModel> rememberViewModelFactory(
//  crossinline builder: @DisallowComposableCalls CreationExtras.() -> VM,
// ): ViewModelFactory<VM> =
//  remember { viewModelFactory(builder) }
//
// /**
// * Remember a [ViewModelFactory] that will be used to create a [ViewModel] with the given [key].
// *
// * @see remember
// * @see viewModelFactory
// */
// @Composable
// public inline fun <VM : ViewModel> rememberViewModelFactory(
//  key: Any?,
//  crossinline builder: @DisallowComposableCalls CreationExtras.() -> VM,
// ): ViewModelFactory<VM> =
//  remember(key) { viewModelFactory(builder) }
