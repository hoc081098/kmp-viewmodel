package com.hoc081098.solivagant.sample.todo.domain

import kotlinx.coroutines.flow.Flow

interface TodoItemRepository {
  fun observeAll(): Flow<List<TodoItem>>

  fun observeById(id: TodoItem.Id): Flow<TodoItem?>

  suspend fun add(
    text: TodoItem.Text,
    isDone: Boolean,
  ): Result<TodoItem>

  suspend fun removeById(id: TodoItem.Id): Result<Unit>

  suspend fun toggleById(id: TodoItem.Id): Result<TodoItem>

  suspend fun update(item: TodoItem): Result<TodoItem>
}
