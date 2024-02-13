package com.hoc081098.kmp.viewmodel.utils

import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal expect class TestAtomicBoolean(value: Boolean = false) {
  var value: Boolean
}

internal fun TestAtomicBoolean.delegated(): PropertyDelegateProvider<Any?, ReadWriteProperty<Any?, Boolean>> =
  PropertyDelegateProvider { _, _ ->
    object : ReadWriteProperty<Any?, Boolean> {
      override fun getValue(thisRef: Any?, property: KProperty<*>): Boolean = value

      override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {
        this@delegated.value = value
      }
    }
  }
