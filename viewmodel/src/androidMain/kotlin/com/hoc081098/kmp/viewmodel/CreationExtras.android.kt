package com.hoc081098.kmp.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras as AndroidXCreationExtras
import androidx.lifecycle.viewmodel.CreationExtras.Key as AndroidXCreationExtrasKey
import androidx.lifecycle.viewmodel.MutableCreationExtras as AndroidXMutableCreationExtras

public actual typealias CreationExtras = AndroidXCreationExtras

public actual typealias CreationExtrasKey<T> = AndroidXCreationExtrasKey<T>

public actual typealias EmptyCreationExtras = AndroidXCreationExtras.Empty

public actual typealias MutableCreationExtras = AndroidXMutableCreationExtras

public actual val VIEW_MODEL_KEY: Key<String> get() = ViewModelProvider.NewInstanceFactory.VIEW_MODEL_KEY
