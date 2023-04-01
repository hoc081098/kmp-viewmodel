//
//  CommonUI.swift
//  iosApp
//
//  Created by Petrus Nguyen Thai Hoc on 2/25/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared
import Kingfisher

struct ProductItemRow: View {
  private let item: ProductItemUi
  private let url: URL?

  init(item: ProductItemUi) {
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

struct EmptyProductsView: View {
  var body: some View {
    VStack(alignment: .center) {
      Text("Products is empty")
        .font(.title3)
        .multilineTextAlignment(.center)
        .padding(10)
    }.frame(maxWidth: .infinity)
  }
}

struct EmptyProductsView_Previews: PreviewProvider {
  static var previews: some View {
    EmptyProductsView()
  }
}

struct ErrorView: View {
  let error: KotlinThrowable
  let onRetry: () -> Void
  
  var body: some View {
    VStack(alignment: .center) {
      Text("Error: \(error.message ?? "unknown")")
        .font(.title3)
        .multilineTextAlignment(.center)
        .padding(10)

      Button("Retry") { onRetry() }
    }.frame(maxWidth: .infinity)
  }
}
