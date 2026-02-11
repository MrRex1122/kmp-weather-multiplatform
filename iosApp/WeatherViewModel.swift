import Foundation
import shared

@MainActor
final class WeatherViewModel: ObservableObject {
    enum ViewState {
        case loading
        case loaded(WeatherSnapshot)
        case error(String)
    }

    @Published var state: ViewState = .loading
    @Published var locations: [Location] = []
    @Published var selectedIndex: Int = 0

    private let container = AppContainer()
    private let descriptions = WeatherDescriptions()

    init() {
        let repo = container.locationRepository
        let list = repo.defaultLocations() as? [Location] ?? []
        self.locations = list
        self.selectedIndex = 0
        refresh()
    }

    func selectLocation(index: Int) {
        guard index >= 0, index < locations.count else { return }
        selectedIndex = index
        refresh()
    }

    func refresh() {
        guard !locations.isEmpty else {
            state = .error("No locations configured")
            return
        }
        let location = locations[selectedIndex]
        state = .loading
        container.weatherRepository.getWeather(location: location) { snapshot, error in
            DispatchQueue.main.async {
                if let snapshot = snapshot {
                    self.state = .loaded(snapshot)
                } else {
                    let message = error?.localizedDescription ?? "Unknown error"
                    self.state = .error(message)
                }
            }
        }
    }

    func description(for code: Int32) -> String {
        descriptions.forCode(code: code)
    }
}
