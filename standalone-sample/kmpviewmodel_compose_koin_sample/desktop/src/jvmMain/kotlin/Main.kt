import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.hoc081098.common.App

fun main() = application {
  Window(
    onCloseRequest = ::exitApplication,
    title = "KmpViewModel Compose Multiplatform",
  ) {
    App()
  }
}
