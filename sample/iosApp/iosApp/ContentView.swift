import SwiftUI
import shared
import Kingfisher
import Combine

struct ContentView: View {
  @Environment(\.scenePhase) var scenePhase

  @ObservedObject var viewModel = IosProductsViewModel(commonVm: DIContainer.shared.get())

  var body: some View {
    let state = self.viewModel.state

    return VStack {
      ZStack(alignment: .center) {
        if state.isLoading {
          ProgressView("Loading...")
        }
        else if let error = state.error {
          VStack(alignment: .center) {
            Text("Error: \(error.message ?? "unknown")")
              .font(.title3)
              .multilineTextAlignment(.center)
              .padding(10)

            Button("Retry") { self.viewModel.dispatch(action: ProductsActionLoad()) }
          }.frame(maxWidth: .infinity)
        }
        else if state.products.isEmpty {
          VStack(alignment: .center) {
            Text("Products is empty")
              .font(.title3)
              .multilineTextAlignment(.center)
              .padding(10)
          }.frame(maxWidth: .infinity)
        } else {
          List {
            ForEach(state.products, id: \.id) { item in
              ProductItemRow(item: item)
            }
          }.refreshable {
            self.viewModel.dispatch(action: ProductsActionRefresh())

            try? await Task.sleep(nanoseconds: 1_000_000) // 1ms

            // await the first state where isRefreshing is false.
            let _: ProductsState? = await self.viewModel
              .$state
              .first(where: { !$0.isRefreshing })
              .values
              .first(where: { _ in true })
          }
        }
      }.frame(maxHeight: .infinity)
    }.onAppear {
      self.viewModel.dispatch(action: ProductsActionLoad())
      self.viewModel.onActive()
    }
      .onChange(of: scenePhase) { newPhase in
      switch newPhase {
      case .inactive:
        self.viewModel.onInactive()
      case .active:
        self.viewModel.onActive()
      case .background:
        ()
      @unknown default:
        fatalError()
      }
    }
  }
}


struct ProductItemRow: View {
  private let item: ProductItem
  private let url: URL?

  init(item: ProductItem) {
    self.item = item
    self.url = self.item.images.first.flatMap(URL.init(string:))
  }

  var body: some View {
    HStack {
      KFAnimatedImage(url)
        .configure { view in view.framePreloadCount = 3 }
        .cacheOriginalImage()
        .onFailure { e in Napier.e(error: e, "err: url=\(String(describing: url)), e=\(e)") }
        .placeholder { p in ProgressView(p) }
        .fade(duration: 1)
        .forceTransition()
        .aspectRatio(contentMode: .fill)
        .frame(width: 72, height: 72)
        .cornerRadius(20)
        .shadow(radius: 5)
        .frame(width: 92, height: 92)

      VStack(alignment: .leading) {
        Text(item.title)
          .font(.headline)
          .lineLimit(2)
          .truncationMode(.tail)

        Spacer().frame(height: 10)

        Text(item.description_)
          .font(.subheadline)
          .lineLimit(2)
          .truncationMode(.tail)

        Spacer().frame(height: 10)
      }.padding([.bottom, .trailing, .top])
    }
  }
}

struct ContentView_Previews: PreviewProvider {
  static var previews: some View {
    ContentView()
  }
}
