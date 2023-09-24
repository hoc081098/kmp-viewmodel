//
//  ContentView.swift
//  iosApp
//
//  Created by Hoc Nguyen T. on 9/24/23.
//

import SwiftUI
import kmpviewmodel_compose_sample_common

struct IosAppComposeView: UIViewControllerRepresentable {
  func makeUIViewController(context: Context) -> UIViewController {
    IosAppKt.IosAppViewController()
  }

  func updateUIViewController(_ uiViewController: UIViewController, context: Context) { }
}

struct ContentView: View {
  var body: some View {
    IosAppComposeView()
      .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
  }
}

struct ContentView_Previews: PreviewProvider {
  static var previews: some View {
    ContentView()
  }
}
