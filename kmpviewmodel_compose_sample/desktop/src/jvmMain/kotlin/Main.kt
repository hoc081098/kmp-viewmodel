import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.hoc08198.common.App

fun main() = application {
  Window(onCloseRequest = ::exitApplication) {
    App()
  }
}
