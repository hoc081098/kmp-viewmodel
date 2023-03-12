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
          NavigationLink("Products screen", destination: LazyView(ProductsView()))

          Spacer().frame(height: 32)

          NavigationLink("Search products screen", destination: LazyView(SearchProductsView()))

          Spacer().frame(height: 32)

          NavigationLink("Snippets", destination: LazyView(SnippetsView()))
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
