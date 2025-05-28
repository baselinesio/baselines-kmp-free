import BaselinesSampleCompose
import SwiftUI

class AppDelegate: UIResponder, UIApplicationDelegate {

    private let platformComponent: PlatformComponent = PlatformComponentImpl()
    lazy var applicationComponent: IosAppComponent = IosAppComponentCreateComponentKt.createAppComponent(
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
