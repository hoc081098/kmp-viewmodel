//
//  kotlinxCoroutinesFlowExtensions.swift
//  iosApp
//
//  Created by Petrus Nguyen Thai Hoc on 2/12/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation

import Foundation
import shared
import Combine

extension Flow {
  // MARK: - Flow<T>

  func asNonNullPublisher<T: AnyObject>(_ type: T.Type = T.self) -> AnyPublisher<T, Error> {
    NonNullFlowPublisher(flow: self).eraseToAnyPublisher()
  }

  // MARK: - Flow<T?>

  func asNullablePublisher<T: AnyObject>(_ type: T.Type = T.self) -> AnyPublisher<T?, Error> {
    NullableFlowPublisher(flow: self).eraseToAnyPublisher()
  }
}

// MARK: - NonNullFlowPublisher

private struct NonNullFlowPublisher<T: AnyObject>: Publisher {
  typealias Output = T
  typealias Failure = Error

  private let flow: Flow

  init(flow: Flow) {
    self.flow = flow
  }

  func receive<S>(subscriber: S) where S: Subscriber, Error == S.Failure, T == S.Input {
    let subscription = NonNullFlowSubscription(
      flow: flow,
      subscriber: subscriber
    )
    subscriber.receive(subscription: subscription)
  }
}

private class NonNullFlowSubscription<T: AnyObject, S: Subscriber>: Subscription where S.Input == T, S.Failure == Error {

  private var subscriber: S?
  private var closable: Closeable?

  init(
    flow: Flow,
    subscriber: S
  ) {
    self.subscriber = subscriber

    let scope = ImmediateMainScopeKt.ImmediateMainScope()

    self.closable = NonNullFlowWrapper<T>(flow).subscribe(
      scope: scope,
      onValue: {
        _ = subscriber.receive($0)
      },
      onError: {
        subscriber.receive(completion: .failure($0.asNSError()))
      },
      onComplete: {
        subscriber.receive(completion: .finished)
      }
    )
  }

  func request(_ demand: Subscribers.Demand) { }

  func cancel() {
    self.subscriber = nil

    self.closable?.close()
    self.closable = nil
  }
}

// MARK: - NullableFlowPublisher

private struct NullableFlowPublisher<T: AnyObject>: Publisher {
  typealias Output = T?
  typealias Failure = Error

  private let flow: Flow

  init(flow: Flow) {
    self.flow = flow
  }

  func receive<S>(subscriber: S) where S: Subscriber, Error == S.Failure, T? == S.Input {
    let subscription = NullableFlowSubscription(
      flow: flow,
      subscriber: subscriber
    )
    subscriber.receive(subscription: subscription)
  }
}

private class NullableFlowSubscription<T: AnyObject, S: Subscriber>: Subscription where S.Input == T?, S.Failure == Error {

  private var subscriber: S?
  private var closable: Closeable?

  init(
    flow: Flow,
    subscriber: S
  ) {
    self.subscriber = subscriber

    let scope = ImmediateMainScopeKt.ImmediateMainScope()

    self.closable = NullableFlowWrapper<T>(flow).subscribe(
      scope: scope,
      onValue: {
        _ = subscriber.receive($0)
      },
      onError: {
        subscriber.receive(completion: .failure($0.asNSError()))
      },
      onComplete: {
        subscriber.receive(completion: .finished)
      }
    )
  }

  func request(_ demand: Subscribers.Demand) { }

  func cancel() {
    self.subscriber = nil

    self.closable?.close()
    self.closable = nil
  }
}
