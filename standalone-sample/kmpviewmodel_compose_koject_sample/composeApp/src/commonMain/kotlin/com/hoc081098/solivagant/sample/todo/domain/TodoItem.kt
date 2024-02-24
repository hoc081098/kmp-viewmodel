package com.hoc081098.solivagant.sample.todo.domain

import kotlin.jvm.JvmInline

data class TodoItem(
  val id: Id,
  val text: Text,
  val isDone: Boolean,
) {
  @JvmInline
  value class Id(val value: String)

  @JvmInline
  value class Text private constructor(val value: String) {
    init {
      require(value.isNotBlank()) { "text cannot be blank " }
    }

    companion object {
      fun of(value: String): Result<Text> = runCatching { Text(value) }
    }
  }
}
