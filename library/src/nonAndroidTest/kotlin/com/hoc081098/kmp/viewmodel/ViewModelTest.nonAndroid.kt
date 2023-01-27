package com.hoc081098.kmp.viewmodel

import kotlinx.coroutines.launch
import kotlin.test.Test

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