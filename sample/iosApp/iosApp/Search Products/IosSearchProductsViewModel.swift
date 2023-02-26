//
//  IosSearchProductsViewModel.swift
//  iosApp
//
//  Created by Petrus Nguyen Thai Hoc on 2/25/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation


import Foundation
import shared
import Combine

@MainActor
class IosSearchProductsViewModel: ObservableObject {
  private let commonVm: SearchProductsViewModel

  @Published private(set) var state: SearchProductsState
  @Published private(set) var term: String = ""

  init(commonVm: SearchProductsViewModel) {
    self.commonVm = commonVm

    self.state = self.commonVm.stateFlow.typedValue()
    self.commonVm.stateFlow.subscribeNonNullFlow(
      scope: self.commonVm.viewModelScope,
      onValue: { [weak self] in self?.state = $0 }
    )

    self.commonVm
      .searchTermStateFlow
      .asNonNullPublisher(NSString.self)
      .handleEvents(receiveCancel: { Napier.d("searchTermStateFlow cancelled") })
      .assertNoFailure()
      .map { $0 as String }
      .assign(to: &$term)
  }

  func search(term: String) {
    self.commonVm.search(term: term)
  }

  deinit {
    self.commonVm.clear()
    Napier.d("\(self)::deinit")
  }
}
