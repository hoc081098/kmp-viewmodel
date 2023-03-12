//
//  callback_to_async.swift
//  iosApp
//
//  Created by Petrus Nguyen Thai Hoc on 3/12/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation

private class DemoError: Error { }

private final class GetDataRequest: @unchecked Sendable {
  private let lock = NSLock()
  private var item: DispatchWorkItem?
  private var onCancel: (@Sendable () -> Void)?

  func start(
    block: @Sendable @escaping (Result<Int, Error>) -> Void,
    onCancel: @Sendable @escaping () -> Void
  ) {
    self.lock.lock()
    defer { self.lock.unlock() }
    print("Started")

    self.item = .init {
      block(
        Bool.random()
          ? .success(.random(in: 0...1_000))
        : .failure(DemoError())
      )
    }
    self.onCancel = onCancel

    DispatchQueue
      .global(qos: .userInitiated)
      .asyncAfter(deadline: .now() + 2, execute: self.item!)
  }

  func cancel() {
    self.lock.lock()
    defer { self.lock.unlock() }

    item?.cancel()
    item = nil

    onCancel?()
    onCancel = nil
    print("Cancelled")
  }
}

private func getData() async throws -> Int {
  let req = GetDataRequest()

  return try await withTaskCancellationHandler(
    operation: {
      // This check is necessary in case this code runs after the task was cancelled.
      // In which case we want to bail right away.
      try Task.checkCancellation()

      return try await withCheckedThrowingContinuation { cont in
        req.start(
          block: {
            // This check is necessary in case this code runs after the task was cancelled.
            // In which case we want to bail right away.
            guard !Task.isCancelled else { return cont.resume(throwing: CancellationError()) }

            cont.resume(with: $0)
          },
          onCancel: {
            cont.resume(throwing: CancellationError())
          }
        )
      }
    },
    onCancel: { req.cancel() }
  )
}

private func use() {
  let task = Task.detached {
    do { print("Result:", try await getData()) }
    catch { print("Error:", error) }
  }
  DispatchQueue.main.asyncAfter(deadline: .now() + 10) {
    print("Start cancelling")
    task.cancel()
  }
}
