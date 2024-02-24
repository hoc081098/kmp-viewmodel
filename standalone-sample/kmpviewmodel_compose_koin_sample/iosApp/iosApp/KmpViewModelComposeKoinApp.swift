//
//  KmpViewModelComposeKoinApp.swift
//  iosApp
//
//  Created by Hoc Nguyen T. on 9/24/23.
//

import SwiftUI
import kmpviewmodel_compose_sample_common

@main
struct KmpViewModelComposeKoinApp: App {
  init() {
    AppKt.startKoinCommon { _ in }
  }

  var body: some Scene {
    WindowGroup {
      ContentView()
    }
  }
}
