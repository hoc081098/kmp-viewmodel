package com.hoc081098.kmp.viewmodel.koin.compose

import com.hoc081098.kmp.viewmodel.InternalKmpViewModelApi
import kotlin.test.Test
import kotlin.test.assertEquals
import org.koin.core.qualifier.StringQualifier

/**
 * [Reference](https://github.com/InsertKoinIO/koin/blob/74f91987ef94e63e8ea23ac9ed0ce24d6650d742/projects/android/koin-android/src/test/java/org/koin/test/android/viewmodel/ViewModelKeyTest.kt)
 */
@OptIn(InternalKmpViewModelApi::class)
class ViewModelKeyTest {
  @Test
  fun generateRightKey() {
    val q = StringQualifier("_qualifier_")
    val key = "_KEY_"
    val className = "com.hoc081098.GodIsLove"

    assertEquals(
      expected = q.value + "_" + className,
      actual = getViewModelKey(qualifier = q, key = null, className = className)
    )
    assertEquals(
      expected = key,
      actual = getViewModelKey(qualifier = q, key = key, className = className)
    )
    assertEquals(
      expected = null,
      actual = getViewModelKey(qualifier = null, key = null, className = className)
    )
  }
}
