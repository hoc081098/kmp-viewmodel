//
//  ProductDetailView.swift
//  iosApp
//
//  Created by Petrus Nguyen Thai Hoc on 3/4/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared
import Kingfisher

struct ProductDetailView: View {
  @Environment(\.scenePhase) var scenePhase

  @ObservedObject var viewModel: IosProductDetailViewModel

  init(id: Int32) {
    self.viewModel = IosProductDetailViewModel(id: id)
  }

  var body: some View {
    let state = self.viewModel.state

    return ZStack(alignment: .center) {
      ZStack(alignment: .center) {
        if state is ProductDetailStateLoading {
          ProgressView("Loading...")
        }
        if let state = state as? ProductDetailStateError {
          ErrorView(
            error: state.error,
            onRetry: { self.viewModel.retry() }
          )
        }
        if let state = state as? ProductDetailStateSuccess {
          DetailContentView(product: state.product)
        }
      }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .navigationTitle("Product detail")
    }
      .frame(maxWidth: .infinity, maxHeight: .infinity)
      .onAppear { self.viewModel.refresh() }
      .onChange(of: scenePhase) { newPhase in
      if case .active = newPhase {
        self.viewModel.refresh()
      }
    }
  }
}

struct ProductDetailView_Previews: PreviewProvider {
  static var previews: some View {
    ProductDetailView(id: 0)
  }
}


private struct DetailContentView: View {
  let product: ProductItemUi

  private let tuples: [(String, String)]

  init(product: ProductItemUi) {
    self.product = product

    self.tuples = [
      ("Title: ", product.title),
      ("Price: ", String(product.price)),
      ("Description: ", product.description_),
      ("Category: ", product.category.name),
      ("Created at: ", product.creationAt.toDate(.isoDateTimeMilliSec)?.toLongString ?? "unknown"),
      ("Updated at: ", product.updatedAt.toDate(.isoDateTimeMilliSec)?.toLongString ?? "unknown"),
    ]
  }

  var body: some View {
    ScrollView {
      VStack {
        KFAnimatedImage(product.images.first.flatMap(URL.init(string:)))
          .configure { view in view.framePreloadCount = 3 }
          .cacheOriginalImage()
          .onFailure { e in
          Napier.e(
            error: e,
            "err: url=\(String(describing: product.images.first)), e=\(e)"
          )
        }
          .placeholder { p in ProgressView(p) }
          .fade(duration: 1)
          .forceTransition()
          .aspectRatio(1, contentMode: .fill)
          .frame(
          width: UIScreen.main.bounds.size.width - 96,
          height: UIScreen.main.bounds.size.width - 96
        )
          .cornerRadius(12)
          .shadow(radius: 5)

        Spacer().frame(height: 32)

        ForEach(tuples, id: \.0) {
          SimpleTile(pair: $0)

          Spacer().frame(height: 8)
        }
      }.frame(maxWidth: .infinity)
        .padding()
    }
  }
}

private struct SimpleTile: View {
  let pair: (String, String)

  var body: some View {
    HStack(alignment: .top) {
      Text(pair.0)
        .font(.title3)
        .bold()
        .multilineTextAlignment(.leading)

      Spacer().frame(width: 8)

      Text(pair.1)
        .font(.body)
        .frame(maxWidth: .infinity)
        .multilineTextAlignment(.trailing)
    }.frame(maxWidth: .infinity)
  }
}

extension Date {
  static let longFormatter: DateFormatter = {
    let f = DateFormatter()
    f.dateStyle = .long
    return f
  }()

  var toLongString: String {
    Self.longFormatter.string(from: self)
  }
}
