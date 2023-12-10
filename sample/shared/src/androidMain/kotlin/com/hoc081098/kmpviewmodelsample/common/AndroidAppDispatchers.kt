package com.hoc081098.kmpviewmodelsample.common

import kotlinx.coroutines.Dispatchers

internal class AndroidAppDispatchers : AppDispatchers {
  override val main get() = Dispatchers.Main
  override val immediateMain get() = Dispatchers.Main.immediate
  override val io get() = Dispatchers.IO
  override val default get() = Dispatchers.Default
}
