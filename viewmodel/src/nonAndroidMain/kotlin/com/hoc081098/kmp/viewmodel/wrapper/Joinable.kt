@file:Suppress("SpellCheckingInspection")

package com.hoc081098.kmp.viewmodel.wrapper

import com.hoc081098.kmp.viewmodel.Closeable

/**
 * Has the same meaning as [kotlinx.coroutines.Job.join].
 */
public interface Joinable {
  public suspend fun join()
}

public interface JoinableAndCloseable : Joinable, Closeable
