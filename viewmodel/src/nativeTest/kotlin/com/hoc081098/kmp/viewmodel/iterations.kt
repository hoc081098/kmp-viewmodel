package com.hoc081098.kmp.viewmodel

import com.hoc081098.kmp.viewmodel.internal.synchronized
import kotlin.native.concurrent.AtomicInt
import kotlin.native.concurrent.TransferMode
import kotlin.native.concurrent.Worker
import kotlin.test.Test
import kotlin.test.assertEquals

private const val Iterations = 100
private const val NWorkers = 4
private const val Increments = 500

class SynchronizedTest {
  @Test
  fun stressCounterTest() {
    repeat(Iterations) {
      val workers = Array(NWorkers) { Worker.start() }
      val counter = AtomicInt(0)
      val lockable = Lockable()

      workers.forEach { worker ->
        worker
          .execute(
            TransferMode.SAFE,
            { counter to lockable },
          ) { (count, lockable) ->
            repeat(Increments) {
              fun inc(count: Int, innerBlock: () -> Unit) {
                synchronized(lockable) {
                  if (count == 0) {
                    innerBlock()
                  } else {
                    inc(count - 1, innerBlock)
                  }
                }
              }

              val nestedLocks = (1..3).random()
              inc(nestedLocks) {
                val oldValue = count.value
                count.value = oldValue + 1
              }
            }
          }
      }

      workers.forEach { it.requestTermination().result }
      assertEquals(NWorkers * Increments, counter.value)
    }
  }
}
