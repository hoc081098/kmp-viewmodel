package com.hoc081098.kmpviewmodelsample.common

import com.hoc081098.kmpviewmodelsample.isDebug
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext

@OptIn(ExperimentalStdlibApi::class)
internal suspend inline fun debugCheckImmediateMainDispatcher() {
  if (isDebug()) {
    val dispatcher = currentCoroutineContext()[CoroutineDispatcher]
    check(dispatcher === Dispatchers.Main.immediate) {
      "Expected CoroutineDispatcher to be Dispatchers.Main.immediate but was $dispatcher"
    }
  }
}
