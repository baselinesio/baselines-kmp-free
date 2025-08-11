import BaselinesSampleCompose
import SwiftUI
import UIKit

struct ComposeView: UIViewControllerRepresentable {

    private let uiComponent: IosUiComponent

    init(uiComponent: IosUiComponent) {
        self.uiComponent = uiComponent
    }

    func makeUIViewController(context: Context) -> UIViewController {
        uiComponent.baselineViewController.invoke()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        /* no-op */
    }
}

struct ContentView: View {

    private let uiComponent: IosUiComponent

    init(uiComponent: IosUiComponent) {
        self.uiComponent = uiComponent
    }

    var body: some View {
        ComposeView(uiComponent: uiComponent)
            .ignoresSafeArea(.all, edges: .all)
    }
}
