package com.hoc081098.solivagant.sample.todo.features.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext

@OptIn(ExperimentalStdlibApi::class)
internal suspend inline fun debugCheckImmediateMainDispatcher() {
  if (isDebug()) {
    val dispatcher = checkNotNull(currentCoroutineContext()[CoroutineDispatcher]) {
      "Expected CoroutineDispatcher to be not null"
    }

    check(
      dispatcher === Dispatchers.Main.immediate ||
        !dispatcher.isDispatchNeeded(Dispatchers.Main.immediate),
    ) {
      "Expected CoroutineDispatcher to be Dispatchers.Main.immediate but was $dispatcher"
    }
  }
}
