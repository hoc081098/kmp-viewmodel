package com.hoc081098.kmp.viewmodel

import androidx.lifecycle.viewmodel.CreationExtras as AndroidXCreationExtras
import androidx.lifecycle.viewmodel.CreationExtras.Key as AndroidXCreationExtrasKey
import androidx.lifecycle.viewmodel.MutableCreationExtras as AndroidXMutableCreationExtras

public actual typealias CreationExtras = AndroidXCreationExtras

public actual typealias CreationExtrasKey<T> = AndroidXCreationExtrasKey<T>

public actual typealias EmptyCreationExtras = AndroidXCreationExtras.Empty

public actual typealias MutableCreationExtras = AndroidXMutableCreationExtras
