package com.hoc081098.solivagant.sample.todo.features.add.domain

import com.hoc081098.solivagant.sample.todo.domain.TodoItem
import com.hoc081098.solivagant.sample.todo.domain.TodoItemRepository
import com.moriatsushi.koject.Provides

@Provides
internal class AddTodoItem(
  private val todoItemRepository: TodoItemRepository,
) {
  suspend operator fun invoke(
    text: TodoItem.Text,
    isDone: Boolean,
  ): Result<TodoItem> = todoItemRepository.add(text = text, isDone = isDone)
}
