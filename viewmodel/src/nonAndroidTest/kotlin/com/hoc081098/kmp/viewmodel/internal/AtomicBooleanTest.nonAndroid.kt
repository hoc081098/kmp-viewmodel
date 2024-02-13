package com.hoc081098.kmp.viewmodel.internal

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AtomicBooleanTest {
  @Test
  fun default_initial_value_is_false() {
    assertFalse(AtomicBoolean().value)
    assertTrue(AtomicBoolean(true).value)
  }

  @Test
  fun compareAndSet_success_from_false_to_true() {
    val ref = AtomicBoolean(false)
    val result = ref.compareAndSet(expectedValue = false, newValue = true)
    assertTrue(result)
    assertTrue(ref.value)
  }

  @Test
  fun compareAndSet_fail_from_false_to_true() {
    val ref = AtomicBoolean(false)
    val result = ref.compareAndSet(expectedValue = true, newValue = true)
    assertFalse(result)
    assertFalse(ref.value)
  }

  @Test
  fun compareAndSet_success_from_true_to_false() {
    val ref = AtomicBoolean(true)
    val result = ref.compareAndSet(expectedValue = true, newValue = false)
    assertTrue(result)
    assertFalse(ref.value)
  }

  @Test
  fun compareAndSet_fail_from_true_to_false() {
    val ref = AtomicBoolean(true)
    val result = ref.compareAndSet(expectedValue = false, newValue = false)
    assertFalse(result)
    assertTrue(ref.value)
  }
}
