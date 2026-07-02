# 🌤️ AtmosLive Weather

AtmosLive is a production-grade, offline-first Android weather application built entirely with modern Android development standards. It features a custom Glassmorphism UI, real-time background sync, and an architecture designed to survive cold-boots without network connectivity.

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blue.svg?logo=kotlin)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-Material_3-4285F4.svg?logo=android)](https://developer.android.com/jetpack/compose)
[![Clean Architecture](https://img.shields.io/badge/Architecture-MVVM_%7C_Clean-success.svg)]()
[![License](https://img.shields.io/badge/License-MIT-gray.svg)]()

### 📱 Download the App
**[Download the latest v2.6.0 APK here](https://github.com/devilyash10/dynamic_weather_app/releases/latest)**

---

## 🚀 Key Features

* **Offline-First Resilience:** Built with an OkHttp Cache Interceptor and Room Database. If the app is launched without internet, it seamlessly serves the last synced data without crashing or showing blank screens.
* **Jetpack Glance Widget:** Real-time home screen widget powered by `WorkManager` for battery-efficient background updates.
* **ProGuard / R8 Hardened:** Network Data Transfer Objects (DTOs) are safely annotated, allowing full minification and obfuscation for the release build.
* **Fluid UI/UX:** Built 100% in Jetpack Compose featuring a custom skeleton-shimmer loading state and a bespoke Glassmorphism aesthetic.
* **Smart Search:** Concurrently fetches live weather data for multiple saved cities simultaneously without blocking the main UI thread.

## 🛠️ Tech Stack & Architecture

This project strictly adheres to **Clean Architecture** (Presentation, Domain, and Data layers) utilizing the **MVVM** pattern.

* **UI:** Jetpack Compose, Material 3
* **Dependency Injection:** Dagger Hilt
* **Local Storage:** Room SQLite, DataStore Preferences
* **Networking:** Retrofit2, OkHttp3 (with custom Offline Cache Interceptor), Gson
* **Background Processing:** WorkManager, Jetpack Glance
* **Concurrency:** Kotlin Coroutines & StateFlow
* **API:** Open-Meteo (No API key required)

## 📁 Project Structure highlights

```text
app/src/main/java/dev/yash/dynamicweatherapp/
├── data/           # Remote API implementation, Room DAOs, and OkHttp Interceptors
├── domain/         # Core business logic, Repository interfaces, and Models
├── di/             # Dagger Hilt Modules (NetworkModule, LocationModule, AppModule)
└── presentation/   # Jetpack Compose UI, ViewModels, and State Management