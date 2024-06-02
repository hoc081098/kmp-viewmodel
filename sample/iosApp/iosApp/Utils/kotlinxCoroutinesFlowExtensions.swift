//
//  kotlinxCoroutinesFlowExtensions.swift
//  iosApp
//
//  Created by Petrus Nguyen Thai Hoc on 2/12/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import Combine

extension Kotlinx_coroutines_coreFlow {
  // MARK: - Flow<T>

  func asNonNullPublisher<T: AnyObject>(
    _ type: T.Type = T.self,
    dispatcher: Kotlinx_coroutines_coreCoroutineDispatcher =
      CoroutinesUtils.shared.coroutineDispatchers().Unconfined
  ) -> AnyPublisher<T, Error> {
    NonNullFlowPublisher(flow: self, dispatcher: dispatcher)
      .eraseToAnyPublisher()
  }

  // MARK: - Flow<T?>

  func asNullablePublisher<T: AnyObject>(
    _ type: T.Type = T.self,
    dispatcher: Kotlinx_coroutines_coreCoroutineDispatcher =
      CoroutinesUtils.shared.coroutineDispatchers().Unconfined
  ) -> AnyPublisher<T?, Error> {
    NullableFlowPublisher(flow: self, dispatcher: dispatcher)
      .eraseToAnyPublisher()
  }
}

// MARK: - NonNullFlowPublisher

private struct NonNullFlowPublisher<T: AnyObject>: Publisher {
  typealias Output = T
  typealias Failure = Error

  private let flow: Kotlinx_coroutines_coreFlow
  private let dispatcher: Kotlinx_coroutines_coreCoroutineDispatcher

  init(flow: Kotlinx_coroutines_coreFlow, dispatcher: Kotlinx_coroutines_coreCoroutineDispatcher) {
    self.flow = flow
    self.dispatcher = dispatcher
  }

  func receive<S>(subscriber: S) where S: Subscriber, Error == S.Failure, T == S.Input {
    let subscription = NonNullFlowSubscription(
      flow: flow,
      subscriber: subscriber,
      dispatcher: dispatcher
    )
    subscriber.receive(subscription: subscription)
  }
}

private class NonNullFlowSubscription<T: AnyObject, S: Subscriber>: Subscription where S.Input == T, S.Failure == Error {

  private var subscriber: S?
  private var closable: Closeable?

  init(
    flow: Kotlinx_coroutines_coreFlow,
    subscriber: S,
    dispatcher: Kotlinx_coroutines_coreCoroutineDispatcher
  ) {
    self.subscriber = subscriber

    let wrapper = NonNullFlowWrapperKt.wrap(flow) as! NonNullFlowWrapper<T>
    self.closable = wrapper.subscribe(
      scope: CoroutinesUtils.shared.supervisorJobCoroutineScope(dispatcher: dispatcher),
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

  private let flow: Kotlinx_coroutines_coreFlow
  private let dispatcher: Kotlinx_coroutines_coreCoroutineDispatcher

  init(flow: Kotlinx_coroutines_coreFlow, dispatcher: Kotlinx_coroutines_coreCoroutineDispatcher) {
    self.flow = flow
    self.dispatcher = dispatcher
  }

  func receive<S>(subscriber: S) where S: Subscriber, Error == S.Failure, T? == S.Input {
    let subscription = NullableFlowSubscription(
      flow: flow,
      subscriber: subscriber,
      dispatcher: dispatcher
    )
    subscriber.receive(subscription: subscription)
  }
}

private class NullableFlowSubscription<T: AnyObject, S: Subscriber>: Subscription where S.Input == T?, S.Failure == Error {

  private var subscriber: S?
  private var closable: Closeable?

  init(
    flow: Kotlinx_coroutines_coreFlow,
    subscriber: S,
    dispatcher: Kotlinx_coroutines_coreCoroutineDispatcher
  ) {
    self.subscriber = subscriber

    let wrapper = NullableFlowWrapperKt.wrap(flow) as! NullableFlowWrapper<T>
    self.closable = wrapper.subscribe(
      scope: CoroutinesUtils.shared.supervisorJobCoroutineScope(dispatcher: dispatcher),
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
