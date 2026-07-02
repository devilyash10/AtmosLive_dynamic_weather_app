# Dynamic Weather App - Project Context

## Overview
A modern, single-screen Android weather application built from scratch to demonstrate proficiency in modern Android development. Features a custom dark-themed, glassmorphic UI.

## Tech Stack
* **Language:** Kotlin
* **UI Toolkit:** Jetpack Compose (Material 3)
* **Architecture:** MVVM (Clean Architecture approach)
* **Concurrency:** Coroutines & StateFlow
* **Networking:** Retrofit2 & Gson
* **Location:** FusedLocationProviderClient
* **Background Work:** WorkManager
* **Navigation:** Jetpack Navigation Compose
* **Local Storage:** DataStore (Preferences)

## Core Features
1.  Fetch and display 6+ weather metrics (temp, humidity, wind, sunrise/sunset, condition, UV, pressure).
2.  Automatic GPS-based location updates on startup.
3.  City search functionality with a saved "My Cities" list.
4.  Customizable background sync (15m, 30m, 1h).
5.  Settings configuration (Celsius/Fahrenheit, Display toggles).

# Dynamic Weather App - Project Context

## Roadmap & Status
* [x] **Phase 0:** Initial Project Configuration
* [x] **Phase 1:** UI/UX Finalization & Architecture Mapping
* [x] **Phase 2:** Domain & Data Layer Setup
* [x] **Phase 3:** Dependency Injection & Location Services
* [x] **Phase 4:** Core UI Components & Material 3 Theme
* [x] **Phase 5:** Home Screen Implementation
* [x] **Phase 6:** Search & Settings Implementation
* [ ] **Phase 7:** Polish, Optimization & Release Prep

---

## Detailed Phase Documentation

### Phase 0: Initial Project Configuration
We established the project foundation using Kotlin DSL (build.gradle.kts). We integrated the Jetpack Compose Bill of Materials (BOM) to ensure library compatibility and added essential dependencies including Navigation Compose, Coroutines, Retrofit, FusedLocationProvider, and WorkManager. We also configured the Android Manifest to include permissions for internet access and location tracking. This phase ensured the project environment was stable and ready for architectural implementation.

### Phase 1: UI/UX Finalization & Architecture Mapping
We locked in the Material 3 UI/UX design, featuring a dark-themed aesthetic with glassmorphic cards and dynamic gradients, as seen in our design mockups. We mapped the entire application into a strict MVVM (Model-View-ViewModel) Clean Architecture folder structure. By strictly separating concerns into Data, Domain, and Presentation layers, we ensured that the codebase remains scalable, testable, and maintainable as we add features.

### Phase 2: Domain & Data Layer Setup
We configured the Retrofit network client to consume the OpenWeather One Call API 3.0, which provides efficient, consolidated weather data. We created specific Data Transfer Objects (DTOs) for robust JSON parsing. To isolate our business logic, we created pure Kotlin domain models (`WeatherInfo`) and a `WeatherRepository` interface. Finally, we implemented `WeatherRepositoryImpl` and utilized Kotlin extension functions in `WeatherMappers.kt` to safely transform raw network DTOs into UI-ready domain models, ensuring the Presentation layer remains decoupled from network implementation details.

### Phase 3: Dependency Injection & Location Services
We integrated Dagger Hilt as our Dependency Injection framework, starting with the `DynamicWeatherApp` application class and configuring `MainActivity` as the entry point. We established a clean abstraction for hardware services by defining a `LocationTracker` interface in the Domain layer. We then implemented `DefaultLocationTracker` in the Data layer using `FusedLocationProviderClient`, incorporating safe runtime permission checks to prevent crashes. Finally, we created `AppModule` to manage singleton instances of our Retrofit API, Location Services, and Repositories, ensuring our UI layer can easily and cleanly inject these dependencies.

### Phase 4: Core UI Components & Material 3 Theme
We translated the UI design system into Jetpack Compose, establishing a premium, unified dark theme. We defined specific color palettes (`NimbusDark`, `NimbusGlass`) and custom typography in our theme files. We built a highly reusable `GlassCard` composable to serve as the foundation for our translucent UI containers. Finally, we set up the navigation structure using a type-safe `Screen` sealed class, implemented the `CustomBottomNavBar`, and created a utility to map API weather codes to local image assets.
---
### Note On HomeViewModel
Encapsulation: We use a private _state (MutableStateFlow) and expose a public state (StateFlow). This enforces a unidirectional data flow—the UI can only read the state, never write to it directly.
The .fold function: This is a very elegant Kotlin standard library feature for handling the Result class we created in our Repository. It cleanly forces you to handle both the onSuccess and onFailure paths.

### Phase 5: Home Screen Implementation
We brought the application to life by implementing the `HomeScreen` and its governing `HomeViewModel`. We utilized the `StateFlow` pattern to manage Loading, Success, and Error states in a unidirectional data flow. The UI was constructed modularly to perfectly match the mockups, assembling custom components including the `LocationHeader`, a massive `MainTemperatureDisplay`, an optimized, horizontally scrolling `HourlyForecastRow`, a 2x2 `WeatherMetricsGrid` (detailing humidity, wind, UV, and pressure), and a vertical `DailyForecastSection`. This phase successfully bridged our pure Kotlin business logic with our Material 3 Compose UI layer.

### Phase 6: Search & Settings Implementation
We scaled out full persistence architectures across the application layer. We integrated Jetpack DataStore to capture localized configuration preferences, constructing full mathematical inversion bridges to allow real-time toggling between Celsius and Fahrenheit parameters cleanly across all screen metrics. We decoupled from OpenWeather API dependencies to transition to Open-Meteo's subscription-free tiers. Finally, we added an Android Room SQLite Database ecosystem to handle localized storage parameters for custom user locations, tracking insertions, deletions, and continuous flow bindings inside our debounced `SearchScreen` workflow module.

---

## Current Working Focus
Moving into **Phase 7: Polish, Optimization & Release Prep**. The final goal is to connect the database to the Home Screen so clicking a saved city switches the home weather view, build out empty states, ensure dark-mode status bar formatting, run performance tracing, and polish transitions.

## "feat(ui):implement glassmorphism, custom icons, and complete Settings screen
Extensive UI/UX overhaul to match premium design mockups across the app.

1. Common Components & Assets:
- Upgraded `GlassCard` with linear gradients and translucent borders for a realistic frosted glass effect.
- Imported and implemented custom vector SVG weather icons (fixing W3C DOCTYPE 429 import errors).
- Refactored `WeatherIconMapper` to support a hybrid of OpenWeatherMap strings (01d/01n for day/night support) and Open-Meteo integer codes.

2. Home Screen:
- Rebuilt `DailyForecastSection`: moved header outside the card, added layered gradient temperature bars, and added subtle row dividers.
- Removed hardcoded white tints in `HourlyForecastRow` and `MainTemperatureDisplay` to allow custom SVG colors to render.
- Updated `WeatherMetricsGrid` to utilize custom drawable icons (humidity, wind, pressure, etc.) via `painterResource`.

3. Search Screen:
- Rebuilt `SearchScreen` UI to match mockups with live weather data for saved cities.
- Updated `SearchState` and `SearchViewModel` to fetch live weather data for all saved database entities concurrently using Coroutines.
- Replaced the delete icon with a long-press gesture and added a subtle, dimmed instructional hint.

4. Settings Screen:
- Built full `SettingsScreen` UI matching target mockups (Preferences, Background Sync, Display, About).
- Implemented `SettingsViewModel` with DataStore integration for global temperature unit toggling.
- Built a custom, animated glassmorphic dialogue box overlay for the 'About the Developer' section.
  "



git commit -m "feat(navigation): implement premium screen transitions and tab state management" -m "
Upgraded app navigation architecture to improve user experience and memory efficiency.

1. Bottom Navigation Polish:
- Implemented robust backstack management in CustomBottomNavBar using `saveState` and `restoreState`.
- Prevented infinite stack growth by popping to `findStartDestination().id` on tab selection.
- Tabs now correctly retain their individual states (e.g., search queries persist when switching tabs).

2. Screen Transitions:
- Replaced default instant cuts with built-in Jetpack Compose AnimatedContentTransitionScope.
- Configured global `enterTransition` and `exitTransition` to slide left/right with a 300ms tween.
- Configured corresponding `popEnterTransition` and `popExitTransition` to reverse the slide on back navigation.
- Fixed Scaffold `paddingValues` alignment in MainActivity to prevent bottom bar UI overlap.
  "


  

git commit -m "feat(sync): implement background weather sync and severe weather alerts" -m "
Built a battery-optimized background engine using WorkManager and DataStore.

1. Background Engine:
- Implemented WeatherSyncWorker to silently fetch background updates for saved locations.
- Integrated HiltWorkerFactory in DynamicWeatherApp to enable dependency injection (Repository/Dao) inside WorkManager.
- Created WeatherWorkScheduler to handle periodic requests based on network and battery constraints.

2. DataStore Persistence:
- Refactored SettingsRepository and SettingsRepositoryImpl to persistently store the user's sync interval preferences.
- Wired SettingsViewModel to emit persistent Flow states to the Settings UI, replacing temporary memory states.
- Bound Settings UI radio buttons (Never, 15, 30, 60, 360 mins) to DataStore logic.

3. Severe Weather Alerts:
- Added Android 13+ POST_NOTIFICATIONS permission and created the 'WEATHER_ALERTS' notification channel.
- Programmed WeatherSyncWorker to evaluate WMO icon codes (65, 82, 95, 96, 99, 11d, 11n, 09d, 09n) and trigger high-priority push notifications when severe weather is detected.
  "

git commit -m "feat(widget): implement Jetpack Glance home screen widget" -m "
Built a modern, Compose-style home screen widget for quick weather access.

1. Widget Architecture:
- Implemented WeatherWidget using GlanceAppWidget and GlanceModifier for a glassmorphic UI.
- Created WeatherWidgetReceiver to handle Android widget broadcast events.
- Registered widget metadata (weather_widget_info.xml) specifying sizing and grid dimensions.

2. Data Integration & UI Fixes:
- Configured WidgetEntryPoint to allow the Glance widget to safely inject LocationDao and WeatherRepository.
- Programmed the widget to query the local Room database for the first saved city and fetch live background updates.
- Resolved ColorProvider Day/Night mode compilation requirements for custom Glance text and background colors.
  "

git commit -m "feat(ui): implement premium empty states for networking and search" -m "
Finalized UI/UX edge cases to ensure graceful degradation and intuitive onboarding.

1. Error Handling:
- Created reusable EmptyStateView with glassmorphic styling and a retry callback.
- Refactored HomeScreen to isolate vertical scrolling exclusively to the successful data state, fixing alignment issues for loading spinners and error screens.

2. Search Onboarding:
- Implemented an intuitive empty state in SearchScreen to guide new users to use the search bar when the Room database is empty.
- Applied consistent Nimbus theme styling across all fallback UIs.
  "
  git commit -m "chore(release): finalize onboarding, privacy policy, and UI polish" -m "
  Completed final pre-release checklist, ensuring legal compliance, UI consistency, and robust state management.

1. First-Time Onboarding & Privacy:
- Engineered a one-time PrivacyPolicyDialog overlay for first-time app launches.
- Integrated DataStore preferences to persistently track privacy policy acceptance.
- Resolved race conditions between DataStore initialization and Android native permission launchers by utilizing nullable boolean state flows.

2. Navigation & Routing Fixes:
- Fixed CustomBottomNavBar active state tracking. Implemented URL parsing to strip dynamic route arguments (coordinates/city names), ensuring the Home icon correctly highlights regardless of the injected parameters.

3. Typography & UI Polish:
- Scaled up typography across the Settings screen and Dialogs to standard Android reading sizes (14sp/16sp) for improved accessibility.
- Revamped the AboutDeveloperDialog with a personalized professional biography and mandatory Open-Meteo API attribution to ensure legal compliance.
- Removed deprecated UI toggles from the Settings screen to ensure 100% feature functionality for portfolio presentation.
  "


git commit -m "Release v2.6.0: Implement Glance Widget, Offline Support, and Navigation Architecture fixes

Features:
- feat(widget): Implement Jetpack Glance home screen widget with Room DB integration for offline rendering.
- feat(settings): Overhaul SettingsScreen with a premium Glassmorphism UI, sync intervals, and dedicated Privacy/About dialogs.
- feat(home): Add smart refresh ticker to LocationHeader to calculate exact 'time ago' since last sync.

Architecture & Bug Fixes:
- fix(navigation): Resolve backstack entanglement in SearchScreen and CustomBottomNavBar using popUpTo and restoreState, preventing duplicate screen stacking.
- fix(lifecycle): Implement state-holding ViewModels to prevent redundant Open-Meteo API calls on tab re-selection.
- fix(ui): Prevent location permission launcher loop on returning to HomeScreen.

Build & Security:
- chore(build): Bump version code to 6 and version name to 2.6.0.
- chore(security): Add proguard-rules.pro to protect network DTOs, Room Entities, and Gson parsers from aggressive R8 minification.
- docs(readme): Update README.md with production architecture details, offline-first capabilities, and v2.6.0 release links."
