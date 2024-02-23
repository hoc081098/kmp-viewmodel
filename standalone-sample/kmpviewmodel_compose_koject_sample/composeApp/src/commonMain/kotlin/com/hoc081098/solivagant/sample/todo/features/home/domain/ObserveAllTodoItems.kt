package com.hoc081098.solivagant.sample.todo.features.home.domain

import com.hoc081098.solivagant.sample.todo.domain.TodoItem
import com.hoc081098.solivagant.sample.todo.domain.TodoItemRepository
import com.moriatsushi.koject.Provides
import kotlinx.coroutines.flow.Flow

@Provides
internal class ObserveAllTodoItems(
  private val todoItemRepository: TodoItemRepository,
) {
  operator fun invoke(): Flow<List<TodoItem>> = todoItemRepository.observeAll()
}
