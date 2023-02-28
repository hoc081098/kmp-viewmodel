package com.hoc081098.kmpviewmodelsample

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher

interface AppDispatchers {
  val main: MainCoroutineDispatcher
  val immediateMain: MainCoroutineDispatcher
  val io: CoroutineDispatcher
  val default: CoroutineDispatcher
}
