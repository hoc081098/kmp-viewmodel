package com.hoc081098.kmp.viewmodel

import kotlin.test.Test
import kotlinx.coroutines.launch

class DemoViewModel : ViewModel() {
  val scope get() = super.viewModelScope
}

class ViewModelTest {
  @Test
  fun test() {
    val vm = DemoViewModel()
    vm.scope.launch {
      println("Hello")
    }
    println(vm.scope)
  }
}
