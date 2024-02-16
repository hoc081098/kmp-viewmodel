package com.hoc081098.kmp.viewmodel.koin.compose

import com.hoc081098.kmp.viewmodel.InternalKmpViewModelApi
import kotlin.test.Test
import kotlin.test.assertEquals
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.qualifier.StringQualifier
import org.koin.core.scope.Scope
import org.koin.dsl.koinApplication

/**
 * [Reference](https://github.com/InsertKoinIO/koin/blob/581640c116169a5c0e863f5bccd022be0e4b8915/projects/android/koin-android/src/test/java/org/koin/test/android/viewmodel/ViewModelKeyTest.kt)
 */
@OptIn(KoinInternalApi::class, InternalKmpViewModelApi::class)
class ViewModelKeyTest {
  @Test
  fun generateRightKey() {
    val koin = koinApplication().koin
    val root = koin.scopeRegistry.rootScope

    val q = StringQualifier("_qualifier_")
    val nonRootScope = Scope(
      scopeQualifier = StringQualifier(value = "_q_"),
      id = "_id_",
      _koin = koin,
      isRoot = false,
    )
    val key = "_KEY_"

    assertEquals(
      expected = null,
      actual = getViewModelKey(qualifier = null, scope = root, key = null)
    )
    assertEquals(
      expected = q.value,
      actual = getViewModelKey(qualifier = q, scope = root, key = null)
    )
    assertEquals(
      expected = key,
      actual = getViewModelKey(qualifier = null, scope = root, key = key)
    )
    assertEquals(
      expected = nonRootScope.id,
      actual = getViewModelKey(qualifier = null, scope = nonRootScope, key = null)
    )

    assertEquals(
      expected = key + nonRootScope.id,
      actual = getViewModelKey(qualifier = null, scope = nonRootScope, key = key)
    )

    assertEquals(
      expected = q.value + key + nonRootScope.id,
      actual = getViewModelKey(qualifier = q, scope = nonRootScope, key = key)
    )
  }
}
