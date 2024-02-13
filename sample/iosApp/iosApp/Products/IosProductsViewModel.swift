//
//  IosProductsViewModel.swift
//  iosApp
//
//  Created by Petrus Nguyen Thai Hoc on 2/12/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import Combine

@MainActor
class IosProductsViewModel: ObservableObject {
  private let commonVm: ProductsViewModel = DIContainer.shared.get()

  @Published private(set) var state: ProductsState

  let singleEventPublisher: AnyPublisher<ProductSingleEvent, Never>

  init() {
    self.singleEventPublisher = self.commonVm.eventFlow
      .asNonNullPublisher(
        ProductSingleEvent.self,
        dispatcher: DIContainer.shared
          .get(for: AppDispatchers.self)
          .immediateMain
      )
      .handleEvents(receiveCancel: { Napier.d("eventFlow cancelled") })
      .assertNoFailure()
      .eraseToAnyPublisher()

    self.state = self.commonVm.stateFlow.value
    self.commonVm.stateFlow.subscribe(
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

  // MARK: Demo purpose only
  private var cancellable: AnyCancellable? = nil

  private lazy var tickerPublisher: AnyPublisher<String?, Never> = self.commonVm
    .tickerFlow
    .asNullablePublisher(
      NSString.self,
      dispatcher: DIContainer.shared
        .get(for: AppDispatchers.self)
        .immediateMain
    )
    .map { $0 as String? }
    .assertNoFailure()
    .eraseToAnyPublisher()

  func onActive() {
    self.cancellable?.cancel()
    self.cancellable = self
      .tickerPublisher
      .sink(
      receiveCompletion: { completion in
        switch completion {
        case .finished:
          Napier.d("[ticker]: finished")
        case .failure(let error):
          Napier.e(error: error, "[ticker]: failure")
        }
      },
      receiveValue: { v in Napier.d("[ticker]: value=\(v ?? "nil")") }
    )
  }

  func onInactive() {
    self.cancellable?.cancel()
    self.cancellable = nil
  }
}
