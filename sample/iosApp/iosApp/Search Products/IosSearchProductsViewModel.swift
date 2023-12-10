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
  private let commonVm: SearchProductsViewModel = DIContainer.shared.get()

  @Published private(set) var state: SearchProductsState
  @Published private(set) var term: String = ""

  init() {    
    self.state = self.commonVm.stateFlow.value
    self.commonVm.stateFlow.subscribe(
      scope: self.commonVm.viewModelScope,
      onValue: { [weak self] in self?.state = $0 }
    )

    self.commonVm
      .searchTermStateFlow
      .asNonNullPublisher(
        NSString.self,
        dispatcher: DIContainer.shared
          .get(for: AppDispatchers.self)
          .immediateMain
      )
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
