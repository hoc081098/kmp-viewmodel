package com.hoc081098.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hoc081098.common.navigation.NavHost
import com.hoc081098.common.screens.ScreenA
import com.hoc081098.common.screens.ScreenAContent
import com.hoc081098.common.screens.ScreenBContent
import com.hoc081098.common.screens.ScreenCContent

@Composable
fun App(
  modifier: Modifier = Modifier,
) {
  MaterialTheme {
    Surface(
      modifier = modifier.fillMaxSize(),
      color = MaterialTheme.colors.background,
    ) {
      NavHost(
        initialRoute = ScreenA,
        contents = listOf(
          ScreenAContent,
          ScreenBContent,
          ScreenCContent,
        ),
      )
    }
  }
}
