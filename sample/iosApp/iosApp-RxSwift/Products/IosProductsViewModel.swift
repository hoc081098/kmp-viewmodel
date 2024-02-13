//
//  IosProductsViewModel.swift
//  iosApp-RxSwift
//
//  Created by Petrus Nguyen Thai Hoc on 4/11/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import RxSwift
import RxCocoa
import shared

class IosProductsViewModel {
  private let commonVm = DIContainer.shared.get(for: ProductsViewModel.self)

  let state$: Driver<ProductsState>
  
  init() {
    self.state$ = self.commonVm
      .stateFlow
      .asNonNullObservable(
        ProductsState.self,
        dispatcher: DIContainer.shared
          .get(for: AppDispatchers.self)
          .immediateMain
      )
      .asDriver(onErrorDriveWith: .empty())
  }

  func dispatch(action: ProductsAction) {
    self.commonVm.dispatch(action: action)
  }

  deinit {
    self.commonVm.clear()
    Napier.d("\(self)::deinit")
  }

  // MARK: Demo purpose only

  private let disposable = SerialDisposable()

  private lazy var ticker$: Observable<NSString?> = self.commonVm
    .tickerFlow
    .asNullableObservable(
      NSString.self,
      dispatcher: DIContainer.shared
        .get(for: AppDispatchers.self)
        .immediateMain
    )

  func onActive() {
    Napier.d("onActive")
    self.disposable.disposable = self
      .ticker$
      .subscribe(
        onNext: { v in Napier.d("[ticker]: value=\(v ?? "nil")") },
        onCompleted: { Napier.d("[ticker]: finished") },
        onDisposed: { Napier.d("[ticker]: disposed") }
      )
  }

  func onInactive() {
    Napier.d("onInactive")
    self.disposable.dispose()
  }
}
