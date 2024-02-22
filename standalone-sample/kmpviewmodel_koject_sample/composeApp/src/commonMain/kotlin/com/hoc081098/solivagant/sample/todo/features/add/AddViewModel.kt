package com.hoc081098.solivagant.sample.todo.features.add

import androidx.compose.runtime.Immutable
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.koject.ViewModelComponent
import com.hoc081098.kmp.viewmodel.parcelable.Parcelable
import com.hoc081098.kmp.viewmodel.parcelable.Parcelize
import com.hoc081098.kmp.viewmodel.safe.NonNullSavedStateHandleKey
import com.hoc081098.kmp.viewmodel.safe.parcelable
import com.hoc081098.kmp.viewmodel.safe.safe
import com.hoc081098.solivagant.navigation.NavEventNavigator
import com.hoc081098.solivagant.sample.todo.domain.TodoItem
import com.hoc081098.solivagant.sample.todo.features.add.domain.AddTodoItem
import com.hoc081098.solivagant.sample.todo.features.utils.HasSingleEventFlow
import com.hoc081098.solivagant.sample.todo.features.utils.SingleEventChannel
import com.moriatsushi.koject.Provides
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Immutable
internal sealed interface AddUiState : Parcelable {
  @Parcelize
  data class Content(
    val text: String,
    val isDone: Boolean,
    val isIdle: Boolean,
  ) : AddUiState

  @Parcelize
  data object Complete : AddUiState
}

@Immutable
internal sealed interface AddSingleEvent {
  data object ConfirmBack : AddSingleEvent
}

@Provides
internal fun singleEventChannel() = SingleEventChannel<AddSingleEvent>()

@Provides
@ViewModelComponent
internal class AddViewModel(
  private val addTodoItem: AddTodoItem,
  private val navigator: NavEventNavigator,
  private val singleEventChannel: SingleEventChannel<AddSingleEvent>,
  private val savedStateHandle: SavedStateHandle,
) : ViewModel(),
  HasSingleEventFlow<AddSingleEvent> by singleEventChannel {
  internal val uiStateFlow: StateFlow<AddUiState> = savedStateHandle.safe.getStateFlow(UiStateKey)
  internal val backPressesFlow: Flow<Unit> = navigator.backPresses()

  internal fun onTextChange(text: String) {
    savedStateHandle.safe {
      val state = it[UiStateKey]

      it[UiStateKey] = if (state is AddUiState.Content) {
        state.copy(text = text, isIdle = false)
      } else {
        AddUiState.Content(text = text, isDone = false, isIdle = false)
      }
    }
  }

  internal fun onDoneChange(isDone: Boolean) {
    savedStateHandle.safe {
      val state = it[UiStateKey]

      it[UiStateKey] = if (state is AddUiState.Content) {
        state.copy(isDone = isDone, isIdle = false)
      } else {
        AddUiState.Content(text = "", isDone = isDone, isIdle = false)
      }
    }
  }

  internal fun save() {
    viewModelScope.launch {
      val content = savedStateHandle.safe[UiStateKey] as? AddUiState.Content
        ?: return@launch

      TodoItem.Text.of(content.text)
        .mapCatching { text ->
          addTodoItem(
            text = text,
            isDone = content.isDone,
          ).getOrThrow()
        }
        .fold(
          onSuccess = {
            navigator.navigateBack()
            savedStateHandle.safe[UiStateKey] = AddUiState.Complete
          },
          onFailure = {
            // TODO: handle error
          },
        )
    }
  }

  internal fun navigateBack() = navigator.navigateBack()

  internal fun confirmBack() {
    when (val s = savedStateHandle.safe[UiStateKey]) {
      is AddUiState.Content ->
        if (s.isIdle) {
          navigator.navigateBack()
        } else {
          viewModelScope.launch { singleEventChannel.sendEvent(AddSingleEvent.ConfirmBack) }
        }

      else ->
        navigator.navigateBack()
    }
  }

  private companion object {
    private val UiStateKey = NonNullSavedStateHandleKey.parcelable<AddUiState>(
      key = "com.hoc081098.solivagant.sample.todo.features.add.state",
      defaultValue = AddUiState.Content(
        text = "",
        isDone = false,
        isIdle = true,
      ),
    )
  }
}
