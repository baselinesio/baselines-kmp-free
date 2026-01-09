import BaselinesSampleCompose
import SwiftUI

class AppDelegate: UIResponder, UIApplicationDelegate {

    private let platformComponent: IosPlatformComponent = IosPlatformComponentImpl()
    lazy var applicationComponent: IosAppComponent = IosAppComponentKt.createComponent(
            platformComponent: platformComponent
    )

    func application(
            _: UIApplication,
            didFinishLaunchingWithOptions _: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        applicationComponent.compositeInitializer.initialize()
        return true
    }
}
