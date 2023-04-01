@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.hoc081098.kmpviewmodelsample.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hoc081098.kmpviewmodelsample.android.common.MyApplicationTheme
import com.hoc081098.kmpviewmodelsample.android.product_detail.ProductDetailScreen
import com.hoc081098.kmpviewmodelsample.android.products.ProductsScreen
import com.hoc081098.kmpviewmodelsample.android.search_products.SearchProductsScreen

class StartActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      MyApplicationTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background,
        ) {
          val navController = rememberNavController()
          val currentBackStackEntryAsState = navController.currentBackStackEntryAsState()

          val route by rememberCurrentRouteAsState(currentBackStackEntryAsState)
          val previousBackStackEntry by remember(currentBackStackEntryAsState) {
            derivedStateOf {
              currentBackStackEntryAsState.value
              navController.previousBackStackEntry
            }
          }

          Scaffold(
            topBar = {
              TopAppBar(
                title = {
                  Text(
                    text = when (route) {
                      Route.Start -> "KMP ViewModel Sample"
                      Route.Products -> "Products screen"
                      Route.Search -> "Search products screen"
                      Route.ProductDetail -> "Product detail screen"
                      null -> "KMP ViewModel Sample"
                    },
                  )
                },
                navigationIcon = {
                  if (previousBackStackEntry != null) {
                    IconButton(onClick = navController::popBackStack) {
                      Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                      )
                    }
                  }
                },
              )
            },
          ) { innerPadding ->
            AppNavHost(
              modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .fillMaxSize(),
              navController = navController,
            )
          }
        }
      }
    }
  }
}

@Composable
private fun AppNavHost(
  modifier: Modifier = Modifier,
  navController: NavHostController = rememberNavController(),
) {
  NavHost(
    modifier = modifier,
    navController = navController,
    startDestination = Route.Start.route,
  ) {
    composable(Route.Start.routePattern) {
      StartScreen(
        navigateToProducts = { navController.navigate(Route.Products.route) },
        navigateToSearch = { navController.navigate(Route.Search.route) },
      )
    }

    composable(Route.Products.routePattern) {
      ProductsScreen(
        navigateToProductDetail = { id ->
          navController.navigate(
            Route.ProductDetail.route(id = id),
          )
        },
      )
    }

    composable(Route.Search.routePattern) {
      SearchProductsScreen(
        navigateToProductDetail = { id ->
          navController.navigate(
            Route.ProductDetail.route(id = id),
          )
        },
      )
    }

    composable(
      route = Route.ProductDetail.routePattern,
      arguments = listOf(
        Route.ProductDetail.idNavArg,
      ),
    ) {
      ProductDetailScreen()
    }
  }
}

@Composable
private fun StartScreen(
  navigateToProducts: () -> Unit,
  navigateToSearch: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    ProductsButton(
      navigateToProducts = navigateToProducts,
    )

    Spacer(modifier = Modifier.height(16.dp))

    SearchProductsButton(
      navigateToSearch = navigateToSearch,
    )
  }
}

@Composable
private fun SearchProductsButton(
  navigateToSearch: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Button(
    modifier = modifier,
    onClick = navigateToSearch,
  ) {
    Text(
      text = "Search products screen",
    )
  }
}

@Composable
private fun ProductsButton(
  navigateToProducts: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Button(
    modifier = modifier,
    onClick = navigateToProducts,
  ) {
    Text(
      text = "Products screen",
    )
  }
}
