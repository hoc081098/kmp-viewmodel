package com.hoc081098.solivagant.sample.todo.features.home.domain

import com.hoc081098.solivagant.sample.todo.domain.TodoItem
import com.hoc081098.solivagant.sample.todo.domain.TodoItemRepository
import com.moriatsushi.koject.Provides

@Provides
internal class RemoveItemById(
  private val todoItemRepository: TodoItemRepository,
) {
  suspend operator fun invoke(id: TodoItem.Id): Result<Unit> =
    todoItemRepository.removeById(id)
}
