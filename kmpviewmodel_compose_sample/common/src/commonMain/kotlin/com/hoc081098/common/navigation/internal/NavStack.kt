package com.hoc081098.common.navigation.internal

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.hoc081098.common.navigation.Route
import com.hoc081098.common.navigation.RouteContent

internal class NavStack private constructor(
  initialEntries: List<NavEntry<*>>,
  private val onStackEntryRemoved: (NavEntry<*>) -> Unit,
) {
  private val stack: ArrayDeque<NavEntry<*>> = ArrayDeque<NavEntry<*>>()
    .apply { addAll(initialEntries) }

  private val _visibleEntryState = mutableStateOf(stack.last())
  internal val visibleEntryState: State<NavEntry<*>> get() = _visibleEntryState

  private val _canNavigateBackState = mutableStateOf(stack.size > 1)
  internal val canNavigateBackState: State<Boolean> get() = _canNavigateBackState

  private val _isPop = mutableStateOf(false)
  internal val isPop: State<Boolean> get() = _isPop

  /**
   * This is the set of currently running transitions. Use this set to retrieve the entry and call
   * [markTransitionComplete] once the transition is complete.
   */
  private val _transitionsInProgress = mutableStateOf(setOf<NavEntry<*>>())
  internal val transitionsInProgress: State<Set<NavEntry<*>>> get() = _transitionsInProgress

  @Suppress("NOTHING_TO_INLINE")
  private inline fun updateVisibleEntry(isPop: Boolean) {
    _isPop.value = isPop
    _visibleEntryState.value = stack.last()
    _canNavigateBackState.value = stack.size > 1
  }

  internal fun push(entry: NavEntry<*>) {
    stack.add(entry)
    updateVisibleEntry(isPop = false)
  }

  internal fun pushWithTransition(entry: NavEntry<*>) {
    // When passed an entry that is already transitioning via a call to push, ignore the call
    // since we are already moving to the proper state.
    if (
      _transitionsInProgress.value.any { it === entry } &&
      stack.any { it === entry }
    ) {
      return
    }

    val previousEntry = _visibleEntryState.value

    // When navigating, we need to mark the outgoing entry as transitioning until it
    // finishes its outgoing animation.
    _transitionsInProgress.value = buildSet(_transitionsInProgress.value.size + 2) {
      addAll(_transitionsInProgress.value)
      add(previousEntry)
      add(entry)
    }

    push(entry)
  }

  internal fun pop(): NavEntry<*>? {
    check(stack.isNotEmpty()) { "Cannot pop from an empty stack" }

    if (stack.size == 1) {
      // Cannot pop the single entry
      return null
    }

    val removed = stack.removeLast().apply { markAsDestroyed() }
    updateVisibleEntry(isPop = true)
    onStackEntryRemoved(removed)

    return removed
  }

  internal fun popWithTransition() {
    check(stack.isNotEmpty()) { "Cannot pop from an empty stack" }

    if (stack.size == 1) {
      // Cannot pop the single entry
      return
    }

    // When passed an entry that is already transitioning via a call to pop, ignore the call
    // since we are already moving to the proper state.
    val visibleEntry = stack.last().apply { markAsDestroyed() }

    if (_transitionsInProgress.value.any { it === visibleEntry }) {
      return
    }

    // When popping, we need to mark the incoming entry as transitioning
    _transitionsInProgress.value =
      _transitionsInProgress.value + visibleEntry + run { stack.removeLast(); stack.last() }

    updateVisibleEntry(isPop = true)
  }

  /**
   * Convert this [NavStack] to a [Map] that can be used to save state on Android.
   */
  internal fun saveState(): Map<String, ArrayList<out Any>> {
    check(stack.isNotEmpty()) { "Cannot save state of an empty stack" }

    val ids = ArrayList<String>(stack.size)
    val routes = ArrayList<Route>(stack.size)
    stack.forEach {
      ids.add(it.id)
      routes.add(it.route)
    }

    return mapOf(
      SAVED_STATE_IDS to ids,
      SAVED_STATE_ROUTES to routes,
    )
  }

  internal fun markTransitionComplete(entry: NavEntry<*>) {
    _transitionsInProgress.value -= entry

    // If the entry is no longer in the stack, then it was removed during the transition
    // and we should notify the listener.
    if (entry !in stack) {
      onStackEntryRemoved(entry)
      return
    }

    val existing = stack.find { it.id == entry.id }
    if (existing === null || existing.isDestroyed) {
      onStackEntryRemoved(entry)
    }
  }

  companion object {
    private const val SAVED_STATE_IDS = "com.hoc081098.common.navigation.stack.ids"
    private const val SAVED_STATE_ROUTES = "com.hoc081098.common.navigation.stack.routes"

    fun fromSavedState(
      savedState: Map<String, ArrayList<out Any>>,
      contents: List<RouteContent<*>>,
      onStackEntryRemoved: (NavEntry<*>) -> Unit,
    ): NavStack {
      val ids = savedState[SAVED_STATE_IDS] as ArrayList<String>?
        ?: error("Cannot restore NavStack from savedState: missing $SAVED_STATE_IDS")

      val routes = savedState[SAVED_STATE_ROUTES] as ArrayList<Route>?
        ?: error("Cannot restore NavStack from savedState: missing $SAVED_STATE_ROUTES")

      check(ids.size == routes.size) {
        "Cannot restore NavStack from savedState: $SAVED_STATE_IDS and $SAVED_STATE_ROUTES have different sizes"
      }

      val entries = ids.zip(routes) { id, route ->
        NavEntry.create(
          route = route,
          contents = contents,
          id = id
        )
      }

      return NavStack(
        initialEntries = entries,
        onStackEntryRemoved = onStackEntryRemoved,
      )
    }

    fun create(
      initial: NavEntry<*>,
      onStackEntryRemoved: (NavEntry<*>) -> Unit,
    ): NavStack = NavStack(
      initialEntries = listOf(initial),
      onStackEntryRemoved = onStackEntryRemoved,
    )
  }
}
