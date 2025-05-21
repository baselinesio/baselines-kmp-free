import SwiftUI
import BaselinesSampleCompose

@main
struct BaselinesSampleApp: App {

    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    var body: some Scene {
        WindowGroup {
            let uiComponent = delegate.applicationComponent.uiComponentFactory.createUiComponent()
            ContentView(uiComponent: uiComponent)
        }
    }
}
