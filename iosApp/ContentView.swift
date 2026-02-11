import SwiftUI
import shared

struct ContentView: View {
    @StateObject private var viewModel = WeatherViewModel()

    var body: some View {
        NavigationView {
            VStack(spacing: 16) {
                Picker("Location", selection: $viewModel.selectedIndex) {
                    ForEach(0..<viewModel.locations.count, id: \.self) { index in
                        Text(viewModel.locations[index].name)
                            .tag(index)
                    }
                }
                .pickerStyle(.menu)
                .onChange(of: viewModel.selectedIndex) { index in
                    viewModel.selectLocation(index: index)
                }

                Button("Refresh") {
                    viewModel.refresh()
                }

                content
                Spacer()
            }
            .padding(16)
            .navigationTitle("Weather")
        }
    }

    @ViewBuilder
    private var content: some View {
        switch viewModel.state {
        case .loading:
            ProgressView()
        case .error(let message):
            VStack(spacing: 8) {
                Text(message)
                    .foregroundColor(.red)
                Button("Try again") {
                    viewModel.refresh()
                }
            }
        case .loaded(let snapshot):
            WeatherDetails(snapshot: snapshot, descriptions: viewModel)
        }
    }
}

private struct WeatherDetails: View {
    let snapshot: WeatherSnapshot
    let descriptions: WeatherViewModel

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text(snapshot.location.name)
                .font(.title2)

            Text("\(Int(snapshot.current.temperatureC))°C")
                .font(.system(size: 56, weight: .bold))

            Text(descriptions.description(for: snapshot.current.weatherCode))
                .font(.headline)

            Text("Wind \(Int(snapshot.current.windSpeedKmh)) km/h")
                .font(.subheadline)

            Text("7-day forecast")
                .font(.headline)

            let daily = snapshot.daily as? [DailyForecast] ?? []
            ForEach(0..<daily.count, id: \.self) { index in
                let day = daily[index]
                HStack {
                    VStack(alignment: .leading) {
                        Text(day.dateIso)
                        Text(descriptions.description(for: day.weatherCode))
                            .font(.footnote)
                            .foregroundColor(.secondary)
                    }
                    Spacer()
                    Text("\(Int(day.minTempC))° / \(Int(day.maxTempC))°")
                }
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }
}
