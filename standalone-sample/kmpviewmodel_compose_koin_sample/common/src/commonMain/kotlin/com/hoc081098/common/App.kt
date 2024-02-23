package com.hoc081098.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.hoc081098.common.domain.DemoRepository
import com.hoc081098.common.screens.ScreenA
import com.hoc081098.common.screens.ScreenAContent
import com.hoc081098.common.screens.ScreenAViewModel
import com.hoc081098.common.screens.ScreenBContent
import com.hoc081098.common.screens.ScreenBViewModel
import com.hoc081098.common.screens.ScreenCContent
import com.hoc081098.common.screens.ScreenCViewModel
import com.hoc081098.solivagant.navigation.NavEventNavigator
import com.hoc081098.solivagant.navigation.NavHost
import kotlin.experimental.ExperimentalObjCName
import kotlin.jvm.JvmField
import kotlin.native.ObjCName
import kotlinx.collections.immutable.persistentSetOf
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import org.koin.core.context.startKoin
import org.koin.core.module.KoinApplicationDslMarker
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

@JvmField
internal val NavigationModule = module {
  singleOf(::NavEventNavigator)
}

@JvmField
internal val ViewModelModule = module {
  factoryOf(::ScreenAViewModel)
  factoryOf(::ScreenBViewModel)
  factoryOf(::ScreenCViewModel)
}

@JvmField
internal val RepositoryModule = module {
  singleOf(::DemoRepository)
}

@OptIn(ExperimentalObjCName::class)
@ObjCName("startKoinCommon")
@KoinApplicationDslMarker
fun startKoinCommon(appDeclaration: KoinAppDeclaration) {
  startKoin {
    appDeclaration()
    modules(
      NavigationModule,
      ViewModelModule,
      RepositoryModule,
    )
  }
}

@Composable
fun App(modifier: Modifier = Modifier) {
  KoinContext {
    MaterialTheme {
      Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
      ) {
        NavHost(
          startRoute = ScreenA,
          destinations = remember {
            persistentSetOf(
              ScreenAContent,
              ScreenBContent,
              ScreenCContent,
            )
          },
          navEventNavigator = koinInject(),
          destinationChangedCallback = { route ->
            println("Destination changed: $route")
          }
        )
      }
    }
  }
}
