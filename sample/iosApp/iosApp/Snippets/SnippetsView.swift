//
//  IosUserViewModel.swift
//  iosApp
//
//  Created by Petrus Nguyen Thai Hoc on 3/9/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import Combine
import SwiftUI

private actor FakeGetUserUseCaseActor {
  private var count = 0

  func call() async throws -> User? {
    try await Task.sleep(nanoseconds: 1 * 1_000_000_000)

    self.count += 1
    if self.count.isMultiple(of: 2) {
      return nil
    } else {
      return User(id: Int64(count), name: "hoc081098")
    }
  }
}

private class FakeGetUserUseCase: KotlinSuspendFunction0 {
  private let actor = FakeGetUserUseCaseActor()

  func invoke() async throws -> Any? { try await self.`actor`.call() }
}

@MainActor
class IosUserViewModel: ObservableObject {
  private let commonVm: UserViewModel = UserViewModel.init(
    savedStateHandle: .init(),
    getUserUseCase: FakeGetUserUseCase()
  )

  @Published private(set) var user: User?

  init() {
    self.commonVm.userStateFlow.subscribe(
      scope: self.commonVm.viewModelScope,
      onValue: { [weak self] in self?.user = $0 }
    )
  }

  func getUser() { self.commonVm.getUser() }

  deinit {
    self.commonVm.clear()
    Napier.d("\(self)::deinit")
  }
}

struct SnippetsView: View {
  @ObservedObject var viewModel = IosUserViewModel()

  private let timerPublisher = Timer
    .publish(every: 2, on: .main, in: .common)
    .autoconnect()

  var body: some View {
    Text("User: \(viewModel.user?.description() ?? "nil")")
      .onReceive(timerPublisher) { _ in viewModel.getUser() }
  }
}

struct SnippetsView_Previews: PreviewProvider {
  static var previews: some View {
    SnippetsView()
  }
}
