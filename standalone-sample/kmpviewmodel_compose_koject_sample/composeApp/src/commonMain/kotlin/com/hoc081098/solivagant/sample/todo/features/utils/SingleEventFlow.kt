package com.hoc081098.solivagant.sample.todo.features.utils

import com.hoc081098.kmp.viewmodel.Closeable
import com.hoc081098.kmp.viewmodel.MainThread
import com.hoc081098.kmp.viewmodel.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onClosed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.receiveAsFlow

sealed interface SingleEventFlow<E> : Flow<E> {
  /**
   * Must collect in [Dispatchers.Main.immediate][kotlinx.coroutines.MainCoroutineDispatcher.immediate].
   * Safe to call in the coroutines launched by [androidx.lifecycle.lifecycleScope].
   *
   * In Compose, we can use [CollectWithLifecycleEffect] with  [CollectWithLifecycleEffectDispatcher.ImmediateMain].
   */
  @MainThread
  override suspend fun collect(collector: FlowCollector<E>)
}

@MainThread
interface HasSingleEventFlow<E> {
  /**
   * Must collect in [Dispatchers.Main.immediate][kotlinx.coroutines.MainCoroutineDispatcher.immediate].
   * Safe to call in the coroutines launched by [androidx.lifecycle.lifecycleScope].
   *
   * In Compose, we can use [CollectWithLifecycleEffect] with [CollectWithLifecycleEffectDispatcher.ImmediateMain].
   */
  val singleEventFlow: SingleEventFlow<E>
}

@MainThread
sealed interface SingleEventFlowSender<E> {
  /**
   * Must call in [Dispatchers.Main.immediate][kotlinx.coroutines.MainCoroutineDispatcher.immediate].
   * Safe to call in the coroutines launched by [androidx.lifecycle.viewModelScope].
   */
  suspend fun sendEvent(event: E)
}

private class SingleEventFlowImpl<E>(private val channelRef: WeakReference<Channel<E>>) : SingleEventFlow<E> {
  override suspend fun collect(collector: FlowCollector<E>) {
    debugCheckImmediateMainDispatcher()
    val flow = channelRef.get()?.receiveAsFlow() ?: return
    return collector.emitAll(flow)
  }
}

@MainThread
class SingleEventChannel<E> :
  Closeable,
  HasSingleEventFlow<E>,
  SingleEventFlowSender<E> {
  private val _eventChannel = Channel<E>(Channel.UNLIMITED)

  override val singleEventFlow: SingleEventFlow<E> = SingleEventFlowImpl(WeakReference(_eventChannel))

  /**
   * Must be called in Dispatchers.Main.immediate, otherwise it will throw an exception.
   * If you want to send an event from other Dispatcher,
   * use `withContext(Dispatchers.Main.immediate) { eventChannel.send(event) }`
   */
  @MainThread
  override suspend fun sendEvent(event: E) {
    debugCheckImmediateMainDispatcher()

    _eventChannel
      .trySend(event)
      .onClosed { return }
  }

  override fun close() {
    _eventChannel.close()
  }
}

@Suppress("NOTHING_TO_INLINE")
inline fun <E> SingleEventChannel<E>.addToViewModel(viewModel: ViewModel) = apply { viewModel.addCloseable(this) }
