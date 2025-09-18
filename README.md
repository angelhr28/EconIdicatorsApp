
# EconIndicatorsUSD (Android - Compose + Hilt + Retrofit + Room + MVI)

Challenge: Login/Registro/Cerrar sesión nativo + Indicadores económicos con base USD usando https://mindicador.cl/api.
- Últimos valores del día: Dólar/Euro (CLP por unidad) -> Convertidos a base USD (USD=1.0, EUR = EUR/USD, CLP = USD/CLP)
- Gráfico de fluctuación de 6 meses hasta la fecha seleccionada (Canvas Compose).

## Cómo compilar
1) Abrir en Android Studio Iguana o posterior.
2) Sin claves ni secrets: usa mindicador.cl pública.
3) Ejecutar en un emulador o dispositivo: `app` -> `Run`.

## Rutas
- Login -> Indicators (registro disponible en Register)
- Logout en AppBar de Indicators.

## Tech
- Kotlin, Jetpack Compose, Navigation, Hilt, Retrofit/Moshi, OkHttp, Room, Coroutines/Flows.
# EconIdicatorsApp
