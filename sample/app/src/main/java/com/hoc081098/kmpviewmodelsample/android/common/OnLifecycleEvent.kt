package com.hoc081098.kmpviewmodelsample.android.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

@Suppress("unused")
@Composable
fun OnLifecycleEvent(
  vararg keys: Any?,
  lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
  onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit,
) {
  val eventHandler by rememberUpdatedState(onEvent)

  DisposableEffect(*keys, lifecycleOwner) {
    val observer = LifecycleEventObserver { owner, event ->
      eventHandler(owner, event)
    }
    lifecycleOwner.lifecycle.addObserver(observer)

    onDispose {
      lifecycleOwner.lifecycle.removeObserver(observer)
    }
  }
}

typealias LifecycleEventListener = (owner: LifecycleOwner) -> Unit
typealias LifecycleEachEventListener = (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit

@DslMarker
annotation class LifecycleEventBuilderMarker

@Stable
@LifecycleEventBuilderMarker
class LifecycleEventBuilder {
  private var onCreate: LifecycleEventListener? by mutableStateOf(null)
  private var onStart: LifecycleEventListener? by mutableStateOf(null)
  private var onResume: LifecycleEventListener? by mutableStateOf(null)
  private var onPause: LifecycleEventListener? by mutableStateOf(null)
  private var onStop: LifecycleEventListener? by mutableStateOf(null)
  private var onDestroy: LifecycleEventListener? by mutableStateOf(null)
  private var onEach: LifecycleEachEventListener? by mutableStateOf(null)

  @LifecycleEventBuilderMarker
  fun onCreate(block: LifecycleEventListener) {
    onCreate = block
  }

  @LifecycleEventBuilderMarker
  fun onStart(block: LifecycleEventListener) {
    onStart = block
  }

  @LifecycleEventBuilderMarker
  fun onResume(block: LifecycleEventListener) {
    onResume = block
  }

  @LifecycleEventBuilderMarker
  fun onPause(block: LifecycleEventListener) {
    onPause = block
  }

  @LifecycleEventBuilderMarker
  fun onStop(block: LifecycleEventListener) {
    onStop = block
  }

  @LifecycleEventBuilderMarker
  fun onDestroy(block: LifecycleEventListener) {
    onDestroy = block
  }

  @LifecycleEventBuilderMarker
  fun onEach(block: LifecycleEachEventListener) {
    onEach = block
  }

  internal fun buildLifecycleEventObserver() = LifecycleEventObserver { owner, event ->
    when (event) {
      Lifecycle.Event.ON_CREATE -> onCreate
      Lifecycle.Event.ON_START -> onStart
      Lifecycle.Event.ON_RESUME -> onResume
      Lifecycle.Event.ON_PAUSE -> onPause
      Lifecycle.Event.ON_STOP -> onStop
      Lifecycle.Event.ON_DESTROY -> onDestroy
      Lifecycle.Event.ON_ANY -> null
    }?.invoke(owner)

    onEach?.invoke(owner, event)
  }
}

@Composable
fun OnLifecycleEventWithBuilder(
  vararg keys: Any?,
  lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
  builder: LifecycleEventBuilder.() -> Unit,
) {
  val lifecycleEventBuilder = remember { LifecycleEventBuilder() }
  val observer = remember { lifecycleEventBuilder.buildLifecycleEventObserver() }

  // When builder or lifecycleOwner or keys changes, we need to re-execute the effect
  DisposableEffect(builder, lifecycleOwner, *keys) {
    // This make sure all callbacks are always up to date.
    builder(lifecycleEventBuilder)

    lifecycleOwner.lifecycle.addObserver(observer)
    onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
  }
}
