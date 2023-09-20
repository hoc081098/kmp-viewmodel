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

  @Suppress("NOTHING_TO_INLINE")
  private inline fun updateVisibleEntry() {
    _visibleEntryState.value = stack.last()
    _canNavigateBackState.value = stack.size > 1
  }

  internal fun push(entry: NavEntry<*>) {
    stack.add(entry)
    updateVisibleEntry()
  }

  internal fun pop(): NavEntry<*>? {
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
