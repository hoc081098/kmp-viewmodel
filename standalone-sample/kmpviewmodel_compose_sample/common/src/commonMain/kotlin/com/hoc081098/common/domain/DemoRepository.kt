package com.hoc081098.common.domain

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class DemoRepository {
  private var data: String? = null
  private val mutex = Mutex()

  suspend fun save(data: String) = mutex.withLock {
    delay(1_000)
    this.data = data
  }

  suspend fun getData(): String = mutex.withLock { data ?: "No data" }
}
