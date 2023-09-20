package com.hoc08198.common.navigation.internal

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

internal class NavStack(
  initial: NavEntry<*>,
  private val onStackEntryRemoved: (NavEntry<*>) -> Unit,
) {
  private val stack: ArrayDeque<NavEntry<*>> = ArrayDeque<NavEntry<*>>()
    .apply { add(initial) }

  private val _visibleEntryState = mutableStateOf(initial)
  internal val visibleEntryState: State<NavEntry<*>> get() = _visibleEntryState

  fun push(entry: NavEntry<*>) {
    stack.add(entry)
    updateVisibleEntry()
  }

  fun pop(): NavEntry<*>? {
    check(stack.isNotEmpty()) { "Cannot pop from an empty stack" }

    if (stack.size == 1) {
      // Cannot pop the single entry
      return null
    }

    val removed = stack.removeLast()

    updateVisibleEntry()
    onStackEntryRemoved(removed)

    return removed
  }

  private fun updateVisibleEntry() {
    _visibleEntryState.value = stack.last()
  }
}
