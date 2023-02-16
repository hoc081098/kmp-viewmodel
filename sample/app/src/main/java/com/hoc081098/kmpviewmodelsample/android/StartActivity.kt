@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.hoc081098.kmpviewmodelsample.android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

class StartActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      MyApplicationTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background,
        ) {
          Scaffold(
            topBar = {
              TopAppBar(
                title = {
                  Text(
                    text = "KMP ViewModel Sample",
                  )
                },
              )
            },
          ) { innerPadding ->
            Box(
              modifier = Modifier
                .padding(innerPadding)
                .consumedWindowInsets(innerPadding)
                .fillMaxSize(),
              contentAlignment = Alignment.Center,
            ) {
              Button(onClick = {
                startActivity(
                  Intent(
                    this@StartActivity,
                    MainActivity::class.java,
                  ),
                )
              }) {
                Text(
                  text = "Start",
                  style = MaterialTheme.typography.headlineMedium,
                )
              }
            }
          }
        }
      }
    }
  }
}
