package com.hoc081098.solivagant.sample.todo.data

import com.hoc081098.flowext.defer
import com.hoc081098.flowext.select
import com.hoc081098.solivagant.sample.todo.domain.TodoItem
import com.hoc081098.solivagant.sample.todo.domain.TodoItemRepository
import com.moriatsushi.koject.Binds
import com.moriatsushi.koject.Provides
import com.moriatsushi.koject.Singleton
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.internal.SynchronizedObject
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Provides
@Singleton
@Binds
@OptIn(InternalCoroutinesApi::class)
internal class InMemoryTodoItemRepository : TodoItemRepository {
  //region Id generator
  private val idLock = SynchronizedObject()
  private var currentId = 0L
  private fun generateId() = synchronized(idLock) { currentId++ }
    .toString()
    .let(TodoItem::Id)
  //endregion

  private val mutatorMutex = Mutex()
  private var observed = false
  private var lastObservedId = TodoItem.Id("")

  private suspend inline fun fakeTimerDelay() = delay(200.milliseconds)

  private val itemsStateFlow = MutableStateFlow(
    listOf(
      TodoItem(
        id = generateId(),
        text = TodoItem.Text
          .of(value = "solivagant is Compose Multiplatform Navigation library")
          .getOrThrow(),
        isDone = true,
      ),
      TodoItem(
        id = generateId(),
        text = TodoItem.Text
          .of(value = "solivagant: Pragmatic, type safety navigation for Compose Multiplatform")
          .getOrThrow(),
        isDone = false,
      ),
      TodoItem(
        id = generateId(),
        text = TodoItem.Text
          .of(value = "solivagant supports ViewModel, SavedStateHandle, Lifecycle, Multi-Backstacks, Transitions, Back-press handling, and more...")
          .getOrThrow(),
        isDone = false,
      ),
    ),
  )

  override fun observeAll() = defer {
    mutatorMutex.withLock {
      if (observed) {
        observed = false
        delay(1.5.seconds)
      }
    }
    itemsStateFlow
  }

  override fun observeById(id: TodoItem.Id) = defer {
    mutatorMutex.withLock {
      if (lastObservedId != id) {
        lastObservedId = id
        delay(300.milliseconds)
      }
    }
    itemsStateFlow.select { items -> items.find { it.id == id } }
  }

  override suspend fun add(text: TodoItem.Text, isDone: Boolean) = mutatorMutex.withLock {
    fakeTimerDelay()
    val updated = itemsStateFlow.updateAndGet {
      it + TodoItem(
        id = generateId(),
        text = text,
        isDone = isDone,
      )
    }

    Result.success(updated.last())
  }

  override suspend fun removeById(id: TodoItem.Id) = mutatorMutex.withLock {
    fakeTimerDelay()
    itemsStateFlow.update { items -> items.filterNot { it.id == id } }

    Result.success(Unit)
  }

  override suspend fun toggleById(id: TodoItem.Id) = mutatorMutex.withLock {
    fakeTimerDelay()
    val updated = itemsStateFlow.updateAndGet { items ->
      items.map {
        if (it.id == id) it.copy(isDone = !it.isDone)
        else it
      }
    }

    runCatching { updated.first { it.id == id } }
  }

  override suspend fun update(item: TodoItem) = mutatorMutex.withLock {
    fakeTimerDelay()
    itemsStateFlow.update { items ->
      items.map {
        if (it.id == item.id) item
        else it
      }
    }

    Result.success(item)
  }
}
