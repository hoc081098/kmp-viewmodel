@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.hoc081098.kmpviewmodelsample.android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hoc081098.kmpviewmodelsample.android.common.MyApplicationTheme
import com.hoc081098.kmpviewmodelsample.android.products.ProductsActivity
import com.hoc081098.kmpviewmodelsample.android.search_products.SearchProductsActivity

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
              Column(
                horizontalAlignment = Alignment.CenterHorizontally,
              ) {
                ProductsButton()

                Spacer(modifier = Modifier.height(16.dp))

                SearchProductsButton()
              }
            }
          }
        }
      }
    }
  }

  @Composable
  private fun SearchProductsButton() {
    Button(onClick = {
      startActivity(
        Intent(
          this@StartActivity,
          SearchProductsActivity::class.java,
        ),
      )
    }) {
      Text(
        text = "Search products screen",
      )
    }
  }

  @Composable
  private fun ProductsButton() {
    Button(onClick = {
      startActivity(
        Intent(
          this@StartActivity,
          ProductsActivity::class.java,
        ),
      )
    }) {
      Text(
        text = "Products screen",
      )
    }
  }
}
