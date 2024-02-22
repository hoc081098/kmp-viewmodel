package com.hoc081098.solivagant.sample.todo.features.edit

import androidx.compose.runtime.Immutable
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.koject.ViewModelComponent
import com.hoc081098.solivagant.navigation.NavEventNavigator
import com.hoc081098.solivagant.navigation.requireRoute
import com.hoc081098.solivagant.sample.todo.domain.TodoItem
import com.hoc081098.solivagant.sample.todo.features.edit.domain.ObserveTodoItemById
import com.hoc081098.solivagant.sample.todo.features.edit.domain.UpdateTodoItem
import com.hoc081098.solivagant.sample.todo.features.utils.HasSingleEventFlow
import com.hoc081098.solivagant.sample.todo.features.utils.SingleEventChannel
import com.moriatsushi.koject.Provides
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Immutable
internal sealed interface EditUiState {
  data object Loading : EditUiState

  data class Error(val message: String) : EditUiState

  data object NotFound : EditUiState

  data class Content(
    val id: String,
    val text: String,
    val isDone: Boolean,
  ) : EditUiState

  data object Complete : EditUiState
}

@Immutable
internal sealed interface EditSingleEvent {
  data object ConfirmBack : EditSingleEvent
}

@Provides
internal fun singleEventChannel() = SingleEventChannel<EditSingleEvent>()

@Provides
@ViewModelComponent
internal class EditViewModel(
  private val navigator: NavEventNavigator,
  private val observeTodoItemById: ObserveTodoItemById,
  private val updateTodoItem: UpdateTodoItem,
  savedStateHandle: SavedStateHandle,
  private val singleEventChannel: SingleEventChannel<EditSingleEvent>,
) : ViewModel(singleEventChannel),
  HasSingleEventFlow<EditSingleEvent> by singleEventChannel {

  private val route = savedStateHandle.requireRoute<EditScreenRoute>()
  private val _uiStateFlow = MutableStateFlow<EditUiState>(EditUiState.Loading)

  internal val uiStateFlow: StateFlow<EditUiState> = _uiStateFlow.asStateFlow()
  internal val backPressesFlow = navigator.backPresses()

  init {
    viewModelScope.launch {
      _uiStateFlow.value = observeTodoItemById(TodoItem.Id(route.id))
        .map { Result.success(it) }
        .catch { emit(Result.failure(it)) }
        .first()
        .fold(
          onSuccess = { item ->
            if (item == null) {
              EditUiState.NotFound
            } else {
              EditUiState.Content(
                id = item.id.value,
                text = item.text.value,
                isDone = item.isDone,
              )
            }
          },
          onFailure = { EditUiState.Error(it.message ?: "Unknown error") },
        )
    }
  }

  internal fun save() {
    viewModelScope.launch {
      val content = _uiStateFlow.value as? EditUiState.Content
        ?: return@launch

      TodoItem.Text.of(content.text)
        .mapCatching { text ->
          updateTodoItem(
            TodoItem(
              id = TodoItem.Id(content.id),
              text = text,
              isDone = content.isDone,
            ),
          )
            .getOrThrow()
        }
        .fold(
          onSuccess = {
            navigator.navigateBack()
            _uiStateFlow.value = EditUiState.Complete
          },
          onFailure = {
            // TODO: handle error
          },
        )
    }
  }

  internal fun onDoneChange(isDone: Boolean) {
    _uiStateFlow.update {
      when (it) {
        is EditUiState.Content -> it.copy(isDone = isDone)
        else -> it
      }
    }
  }

  internal fun onTextChange(text: String) {
    _uiStateFlow.update {
      when (it) {
        is EditUiState.Content -> it.copy(text = text)
        else -> it
      }
    }
  }

  internal fun navigateBack() = navigator.navigateBack()

  internal fun confirmBack() {
    when (_uiStateFlow.value) {
      is EditUiState.Content -> {
        viewModelScope.launch { singleEventChannel.sendEvent(EditSingleEvent.ConfirmBack) }
      }

      else -> navigator.navigateBack()
    }
  }
}

