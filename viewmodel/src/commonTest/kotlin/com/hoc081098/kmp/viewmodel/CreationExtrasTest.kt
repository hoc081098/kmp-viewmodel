package com.hoc081098.kmp.viewmodel

import kotlin.test.Test
import kotlin.test.assertEquals

// Copied and edit from https://github.com/androidx/androidx/blob/37df1f7745e04a9c7e2a7fb60f7449491276916f/lifecycle/lifecycle-viewmodel/src/androidTest/java/androidx/lifecycle/CreationExtrasTest.kt#L31
class CreationExtrasTest {
  @Test
  fun testInitialCreationExtras() {
    val initial = MutableCreationExtrasBuilder()
    val key = object : CreationExtrasKey<Map<String, String>> {}
    initial[key] = mapOf("value" to "initial")
    val mutable = MutableCreationExtrasBuilder(initial.asCreationExtras())
    initial[key] = mapOf("value" to "overridden")
    assertEquals("initial", mutable[key]?.get("value"))
  }

  @Test
  fun testKtx() {
    val key = object : CreationExtrasKey<Map<String, String>> {}

    val initial = buildCreationExtras {
      this[key] = mapOf("value" to "initial")
    }

    val mutable = initial.edit {
      this[key] = mapOf("value" to "overridden")
    }

    assertEquals("initial", initial[key]?.get("value"))
    assertEquals("overridden", mutable[key]?.get("value"))
  }
}
