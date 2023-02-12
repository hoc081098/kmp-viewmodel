//
//  IosProductsViewModel.swift
//  iosApp
//
//  Created by Petrus Nguyen Thai Hoc on 2/12/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

@MainActor
class IosProductsViewModel: ObservableObject {
  private let commonVm: ProductsViewModel
  
  @Published private(set) var state: ProductsState
  
  init(commonVm: ProductsViewModel) {
    self.commonVm = commonVm
    
    self.state = self.commonVm.stateFlow.typedValue()
    self.commonVm.stateFlow.subscribeNonNullFlow(
      scope: self.commonVm.viewModelScope,
      onValue: { [weak self] in self?.state = $0 }
    )
  }
  
  func dispatch(action: ProductsAction) {
    self.commonVm.dispatch(action: action)
  }

  deinit {
    self.commonVm.clear()
    Napier.d("\(self)::deinit")
  }
}
