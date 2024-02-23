package com.hoc081098.solivagant.sample.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent { TodoApp() }
  }
}

@Preview
@Composable
private fun AppAndroidPreview() {
  TodoApp()
}
