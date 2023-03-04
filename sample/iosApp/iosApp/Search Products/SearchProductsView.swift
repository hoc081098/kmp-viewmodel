//
//  SearchProductsView.swift
//  iosApp
//
//  Created by Petrus Nguyen Thai Hoc on 2/25/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct SearchProductsView: View {
  @ObservedObject var viewModel = IosSearchProductsViewModel()

  var body: some View {
    let state = self.viewModel.state

    let termBinding = Binding<String>(
      get: { self.viewModel.term },
      set: { self.viewModel.search(term: $0) }
    )

    return VStack {
      TextField(
        "Search term",
        text: termBinding
      ).padding()

      ZStack(alignment: .center) {
        if state.isLoading {
          ProgressView("Loading...")
        }
        else if let error = state.error {
          ErrorView(
            error: error,
            onRetry: { }
          )
        }
        else if state.products.isEmpty {
          EmptyProductsView()
        } else {
          List {
            ForEach(state.products, id: \.id) { item in
              NavigationLink(destination: LazyView(ProductDetailView(id: item.id))) {
                ProductItemRow(item: item)
              }
            }
          }
        }
      }.frame(maxHeight: .infinity)
        .navigationTitle("Search products")
    }.frame(maxWidth: .infinity, maxHeight: .infinity)
  }
}

struct SearchProductsView_Previews: PreviewProvider {
  static var previews: some View {
    SearchProductsView()
  }
}
