package com.hoc081098.kmp.viewmodel.koject

import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.createSavedStateHandle
import com.moriatsushi.koject.ExperimentalKojectApi
import com.moriatsushi.koject.component.ComponentExtras

@OptIn(ExperimentalKojectApi::class)
internal class ViewModelComponentExtras(
  private val extras: CreationExtras,
) : ComponentExtras<ViewModelComponent> {
  val savedStateHandle: SavedStateHandle
    get() = extras.createSavedStateHandle()
}

