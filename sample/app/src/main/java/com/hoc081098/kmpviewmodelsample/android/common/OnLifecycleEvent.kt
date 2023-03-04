package com.hoc081098.kmpviewmodelsample.android.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.github.aakira.napier.Napier

@Composable
private fun OnLifecycleEvent(
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

@DslMarker
annotation class LifecycleEventBuilderMarker

@LifecycleEventBuilderMarker
class LifecycleEventBuilder {
  internal var onCreate: LifecycleEventListener? = null
  internal var onStart: LifecycleEventListener? = null
  internal var onResume: LifecycleEventListener? = null
  internal var onPause: LifecycleEventListener? = null
  internal var onStop: LifecycleEventListener? = null
  internal var onDestroy: LifecycleEventListener? = null

  fun onCreate(block: LifecycleEventListener) {
    onCreate = block
  }

  fun onStart(block: LifecycleEventListener) {
    onStart = block
  }

  fun onResume(block: LifecycleEventListener) {
    onResume = block
  }

  fun onPause(block: LifecycleEventListener) {
    onPause = block
  }

  fun onStop(block: LifecycleEventListener) {
    onStop = block
  }

  fun onDestroy(block: LifecycleEventListener) {
    onDestroy = block
  }
}

@Composable
fun OnLifecycleEvent(
  vararg keys: Any?,
  builder: LifecycleEventBuilder.() -> Unit,
) {
  val lifecycleOwner = LocalLifecycleOwner.current

  val lifecycleEventBuilder = remember(lifecycleOwner, *keys) { LifecycleEventBuilder() }

  // On every successful composition, update the lifecycleEventBuilder with the latest builder
  SideEffect {
    Napier.d("[OnLifecycleEvent] update lifecycleEventBuilder with ${System.identityHashCode(builder)})}")
    builder(lifecycleEventBuilder)
  }

  OnLifecycleEvent(keys = keys, lifecycleOwner = lifecycleOwner) { owner, event ->
    when (event) {
      Lifecycle.Event.ON_CREATE -> lifecycleEventBuilder.onCreate?.invoke(owner)
      Lifecycle.Event.ON_START -> lifecycleEventBuilder.onStart?.invoke(owner)
      Lifecycle.Event.ON_RESUME -> lifecycleEventBuilder.onResume?.invoke(owner)
      Lifecycle.Event.ON_PAUSE -> lifecycleEventBuilder.onPause?.invoke(owner)
      Lifecycle.Event.ON_STOP -> lifecycleEventBuilder.onStop?.invoke(owner)
      Lifecycle.Event.ON_DESTROY -> lifecycleEventBuilder.onDestroy?.invoke(owner)
      Lifecycle.Event.ON_ANY -> Unit
    }
  }
}
