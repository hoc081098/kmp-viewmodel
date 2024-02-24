package com.hoc081098.solivagant.sample.todo.features.detail.domain

import com.hoc081098.solivagant.sample.todo.domain.TodoItem
import com.hoc081098.solivagant.sample.todo.domain.TodoItemRepository
import com.moriatsushi.koject.Provides
import kotlinx.coroutines.flow.Flow

@Provides
internal class ObserveTodoItemById(
  private val todoItemRepository: TodoItemRepository,
) {
  operator fun invoke(id: TodoItem.Id): Flow<TodoItem?> = todoItemRepository.observeById(id)
}
