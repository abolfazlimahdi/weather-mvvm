package abolfazli.mahdi.weather.weather.data

import javax.inject.Inject

class WeatherRemoteDataSource @Inject constructor(
    private val weatherService: WeatherService
) {

    suspend fun getCurrentWeatherByLatLon(
        appId: String,
        lat: String,
        lon: String
    )=
        weatherService.fetchCurrentWeatherByLatLon(
             appId,
             lat,
             lon
        )

    suspend fun getCurrentWeatherByCityName(
        appId: String,
        cityName: String
    )=
        weatherService.fetchCurrentWeatherByCityName(
            appId,
            cityName
        )

    suspend fun getHourlyForecastByLatLon(
        appId: String,
        lat: String,
        lon: String
    )=
        weatherService.fetchHourlyForecastByLatLong(
            appId,
            lat,
            lon
        )

    suspend fun getHourlyForecastByCityName(
        appId: String,
        cityName: String
    )=
        weatherService.fetchHourlyForecastByCityName(
            appId,
            cityName
        )

}