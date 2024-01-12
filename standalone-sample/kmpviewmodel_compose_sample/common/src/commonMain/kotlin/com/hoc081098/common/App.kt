package com.hoc081098.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hoc081098.common.domain.DemoRepository
import com.hoc081098.common.navigation.NavHost
import com.hoc081098.common.screens.ScreenA
import com.hoc081098.common.screens.ScreenAContent
import com.hoc081098.common.screens.ScreenAViewModel
import com.hoc081098.common.screens.ScreenBContent
import com.hoc081098.common.screens.ScreenBViewModel
import com.hoc081098.common.screens.ScreenCContent
import com.hoc081098.common.screens.ScreenCViewModel
import kotlin.jvm.JvmField
import org.koin.compose.KoinApplication
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

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

@Composable
fun App(
  modifier: Modifier = Modifier,
) {
  KoinApplication(
    application = {
      modules(
        ViewModelModule,
        RepositoryModule,
      )
    },
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
}
