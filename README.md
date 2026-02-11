# Kotlin Multiplatform Weather

Pet project: Kotlin Multiplatform (Android + iOS) weather app with a shared domain layer and simple UIs.

## Features
- Current weather + 7-day forecast
- Location picker (predefined list)
- Error and loading states
- Shared networking + models in KMP

## Tech Stack
- Kotlin Multiplatform + Ktor
- Android: Jetpack Compose (Material 3)
- iOS: SwiftUI
- Data source: Open-Meteo (no API key required)

## Project Structure
- `shared`: KMP shared module (models, repository, networking)
- `androidApp`: Android app (Compose UI)
- `iosApp`: SwiftUI screens + Podfile

## Run Android
1. Open the project in Android Studio.
2. Sync Gradle.
3. Run the `androidApp` configuration.

## Run iOS
See `iosApp/README.md` for setup steps on macOS.

## Notes
- If you want real location search, add a geocoding provider and replace the static list in `LocationRepository`.
- If Open-Meteo changes its API, update `OpenMeteoApi` and the DTOs in `shared`.
