//
//  async_let.swift
//  iosApp
//
//  Created by Petrus Nguyen Thai Hoc on 3/11/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation

private func getData1() async throws -> Int {
  try await Task.sleep(nanoseconds: 2_000_000_000)
  return 1
}

private func getData2() async throws -> Int {
  try await Task.sleep(nanoseconds: 2_000_000_000)
  return 2
}

private func test() {
  Task {
    async let i1 = getData1()
    async let i2 = getData2()
    let sum = try await (i1 + i2)
    print(sum)
  }
}

private func testGroup() {
  Task {
    let results = try await withThrowingTaskGroup(of: Int.self, returning: [Int].self) { group in
      group.addTask { try await getData1() }
      group.addTask { try await getData2() }

      return try await group.reduce(into: [Int]()) { $0.append($1) }
    }
    print(results)
  }
}
