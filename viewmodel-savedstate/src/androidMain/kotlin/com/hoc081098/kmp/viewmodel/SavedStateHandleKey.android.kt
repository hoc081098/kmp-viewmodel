package com.hoc081098.kmp.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.SavedStateHandle

@SuppressLint("RestrictedApi")
internal actual fun validateValue(value: Any?): Boolean = SavedStateHandle.validateValue(value)
