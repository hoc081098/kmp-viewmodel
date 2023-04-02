//
//  IosProductDetailViewModel.swift
//  iosApp
//
//  Created by Petrus Nguyen Thai Hoc on 3/4/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import Combine

@MainActor
class IosProductDetailViewModel: ObservableObject {
  private let commonVm: ProductDetailViewModel

  @Published var state: ProductDetailState

  init(id: Int32) {
    self.commonVm = DIContainer.shared.get(
      parameters: {
        let parameters: [Any] = [id]
        return DIContainer.shared.parametersOf(parameters: parameters)
      }
    )

    self.state = self.commonVm.stateFlow.value
    self.commonVm.stateFlow.subscribe(
      scope: self.commonVm.viewModelScope,
      onValue: { [weak self] in self?.state = $0 }
    )
  }

  func refresh() { self.commonVm.refresh() }

  func retry() { self.commonVm.retry() }

  deinit {
    self.commonVm.clear()
    Napier.d("\(self)::deinit")
  }
}
