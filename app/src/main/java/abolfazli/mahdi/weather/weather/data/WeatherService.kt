package abolfazli.mahdi.weather.weather.data

import abolfazli.mahdi.weather.weather.data.entities.CurrentWeatherEntity
import abolfazli.mahdi.weather.weather.data.entities.HourlyEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("/data/2.5/weather?units=metric")
    suspend fun fetchCurrentWeatherByCityName(
        @Query("appid") appId: String,
        @Query("q") lat: String
    ): Response<CurrentWeatherEntity>

    @GET("/data/2.5/forecast?units=metric")
    suspend fun fetchHourlyForecastByCityName(
        @Query("appid") appId: String,
        @Query("q") lat: String
    ): Response<HourlyEntity>

    @GET("/data/2.5/weather?units=metric")
    suspend fun fetchCurrentWeatherByLatLon(
        @Query("appid") appId: String,
        @Query("lat") lat: String,
        @Query("lon") lon: String
    )
            : Response<CurrentWeatherEntity>

    @GET("/data/2.5/forecast?units=metric")
    suspend fun fetchHourlyForecastByLatLong(
        @Query("appid") appId: String,
        @Query("lat") lat: String,
        @Query("lon") lon: String
    ): Response<HourlyEntity>


}