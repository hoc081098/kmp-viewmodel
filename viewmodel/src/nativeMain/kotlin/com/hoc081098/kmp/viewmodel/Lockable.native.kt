package com.hoc081098.kmp.viewmodel

import co.touchlab.stately.concurrency.Lock

internal actual open class Lockable {
  internal val lock = Lock()
}
