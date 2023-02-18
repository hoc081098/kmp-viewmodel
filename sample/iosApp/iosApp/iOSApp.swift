import SwiftUI
import shared

@main
struct iOSApp: App {
  init() {
    DIContainer.shared.doInit { _ in }
  }

  var body: some Scene {
    WindowGroup {
      NavigationView {
        VStack(alignment: .center) {
          NavigationLink("START", destination: LazyView(ContentView()))
        }
          .frame(maxWidth: .infinity)
          .navigationTitle("KMP ViewModel sample")
          .navigationBarTitleDisplayMode(.inline)
      }.navigationViewStyle(.stack)
    }
  }
}

struct LazyView<Content: View>: View {
  let build: () -> Content

  init(_ build: @autoclosure @escaping () -> Content) {
    self.build = build
  }

  var body: Content {
    build()
  }
}
