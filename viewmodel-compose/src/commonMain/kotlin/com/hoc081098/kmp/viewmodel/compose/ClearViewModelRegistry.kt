package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.hoc081098.kmp.viewmodel.MainThread

public fun interface Runnable {
  public fun run()
}

public fun interface Cancellable {
  public fun cancel()
}

public interface ClearViewModelRegistry {
  @MainThread
  public fun register(callback: Runnable): Cancellable

  @MainThread
  public fun clear()
}

public class SingleCallbackClearViewModelRegistry : ClearViewModelRegistry {
  private var isCleared = false
  private var callback: (Runnable)? = null

  override fun register(callback: Runnable): Cancellable {
    if (isCleared) {
      callback.run()
      return EmptyCancellable
    }

    this.callback = callback
    return Cancellable { this.callback = null }
  }

  public override fun clear() {
    isCleared = true

    callback?.run()
    callback = null
  }

  private companion object {
    private val EmptyCancellable = Cancellable { }
  }
}

@Composable
public fun rememberSingleCallbackClearViewModelRegistry(): ClearViewModelRegistry =
  remember { SingleCallbackClearViewModelRegistry() }
