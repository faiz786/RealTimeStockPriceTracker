# RealTimeStockPriceTracker

# RealTimeStockPriceTracker  (Jetpack Compose, MVI)

## Overview
This app is a Jetpack Compose MVI implementation of the Real-Time Price Tracker coding challenge.
- Single shared WebSocket connection to `wss://ws.postman-echo.com/raw`.
- Generates and sends mock price updates for 25 symbols every 2 seconds.
- Echoed messages are processed to update UI.
- LazyColumn sorted by price (highest first).
- Top bar has connection indicator, Sun Moon image button to change theme and Start/Stop toggle.
- Details screen for each symbol.

## Architecture
MVI:
- `StockIntent` — UI intents
- `StockAction` — internal actions
- `StockUIState` — immutable UI state
- `StockViewModel` — single source of truth; reduces actions via `StockReducer`

UI: 
- UI is broken into atoms, molecules and organism structure for clean architecture

Dependency Injection Bonus
- Used Hilt Library for dependency Injection to inject view model and web socket manager

## How to run
1. Open in Android Studio by cloning https://github.com/faiz786/RealTimeStockPriceTracker.git.
2. Sync project.
3. Run `app` on an emulator/device with Internet access.

## Notes and Bonus features
- Flashing implemented: Price flashes green/red for 1s on increase/decrease.
- Tests: Test classes added in unit test folder.
- to run test simply go to unit test folder select class RealTimeStockViewModelTest and run it

- ![Stock List Screen](images/test_coverage.png)

## Assumptions & Trade-offs
- Messages use a simple `SYMBOL|PRICE` string format to avoid JSON dependency.
- WebSocket reconnection logic is basic we check if web socket is null we retry; production code may prefer more robust connection state machine and backoff policy.

## Future enhancements
- Adding Room library to store price fluctuations and show graph with MQ chart or some other library.

## Screen Shots
![Stock List Screen](images/initial_stage.png)
![Stock List Screen](images/running_stage.png)
![Details Screen](images/detail_light_screen.png)
![Stock List Screen](images/stock_list_dark.png)
![Details Screen](images/details_dark.png)
