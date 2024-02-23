package com.hoc081098.solivagant.sample.todo.features.home

import androidx.compose.runtime.Immutable
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.koject.ViewModelComponent
import com.hoc081098.solivagant.navigation.NavEventNavigator
import com.hoc081098.solivagant.sample.todo.domain.TodoItem
import com.hoc081098.solivagant.sample.todo.features.add.AddScreenRoute
import com.hoc081098.solivagant.sample.todo.features.detail.DetailScreenRoute
import com.hoc081098.solivagant.sample.todo.features.home.HomeUiState.TodoItemUi
import com.hoc081098.solivagant.sample.todo.features.home.domain.ObserveAllTodoItems
import com.hoc081098.solivagant.sample.todo.features.home.domain.RemoveItemById
import com.hoc081098.solivagant.sample.todo.features.home.domain.ToggleItemById
import com.moriatsushi.koject.Provides
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Immutable
internal sealed interface HomeUiState {
  data object Loading : HomeUiState
  data class Error(val message: String) : HomeUiState
  data class Content(val items: ImmutableList<TodoItemUi>) : HomeUiState

  @Immutable
  data class TodoItemUi(
    val id: String,
    val text: String,
    val isDone: Boolean,
  )
}

@Provides
@ViewModelComponent
internal class HomeViewModel(
  private val navigator: NavEventNavigator,
  private val removeItemById: RemoveItemById,
  private val toggleItemById: ToggleItemById,
  observeAllTodoItems: ObserveAllTodoItems,
) : ViewModel() {
  internal val uiStateFlow: StateFlow<HomeUiState> =
    observeAllTodoItems()
      .onStart { println("${this@HomeViewModel}: observeAllTodoItems start...") }
      .onCompletion { println("${this@HomeViewModel}: observeAllTodoItems completed $it") }
      .map<_, HomeUiState> { items ->
        HomeUiState.Content(
          items = items
            .map { it.toTodoItemUi() }
            .toImmutableList(),
        )
      }
      .catch {
        emit(
          HomeUiState.Error(
            message = it.message ?: "Unknown error",
          ),
        )
      }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = HomeUiState.Loading,
      )

  internal fun toggle(item: TodoItemUi) {
    viewModelScope.launch { toggleItemById(TodoItem.Id(item.id)) }
  }

  internal fun remove(item: TodoItemUi) {
    viewModelScope.launch { removeItemById(TodoItem.Id(item.id)) }
  }

  internal fun navigateToDetail(item: TodoItemUi) =
    navigator.navigateTo(DetailScreenRoute(id = item.id))

  internal fun navigateToAdd() =
    navigator.navigateTo(AddScreenRoute)
}

private fun TodoItem.toTodoItemUi(): TodoItemUi = TodoItemUi(
  id = id.value,
  text = text.value,
  isDone = isDone,
)
