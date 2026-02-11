# iOS App Setup (SwiftUI)

This folder includes SwiftUI screens and a `Podfile` for the shared Kotlin module.

## Setup on macOS
1. Create a new iOS App in Xcode named `iosApp` inside this `iosApp` folder.
2. Ensure the iOS deployment target is **14.0** or higher.
3. Replace the generated SwiftUI files with:
   - `WeatherApp.swift`
   - `ContentView.swift`
   - `WeatherViewModel.swift`
4. From `iosApp`, run:
   - `pod install`
5. Open the generated `.xcworkspace` and build.

The Kotlin shared module is pulled via CocoaPods from `../shared`.
