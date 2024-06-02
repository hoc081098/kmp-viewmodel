package com.hoc081098.kmpviewmodelsample

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Suppress("unused")
object CoroutinesUtils {
  fun coroutineDispatchers(): Dispatchers = Dispatchers

  fun supervisorJobCoroutineScope(dispatcher: CoroutineDispatcher): CoroutineScope =
    CoroutineScope(dispatcher + SupervisorJob())
}

