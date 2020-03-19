package abolfazli.mahdi.weather.weather.ui

import abolfazli.mahdi.weather.weather.data.WeatherRemoteDataSource
import abolfazli.mahdi.weather.weather.data.entities.CurrentWeatherEntity
import abolfazli.mahdi.weather.weather.data.entities.HourlyEntity
import abolfazli.mahdi.weather.weather.data.entities.HourlyListEntity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject


class WeatherViewModel @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource

) : ViewModel() {

    val currentWeather: MutableLiveData<CurrentWeatherEntity> by lazy {
        MutableLiveData<CurrentWeatherEntity>()
    }

    val hourlyForecast: MutableLiveData<HourlyEntity> by lazy {
        MutableLiveData<HourlyEntity>()
    }

    val dailyForecast: MutableLiveData<HourlyEntity> by lazy {
        MutableLiveData<HourlyEntity>()
    }

    val errorString: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }


    fun getCurrentWeatherByCityName(
        appId: String,
        lat: String,
        lon: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = remoteDataSource.getCurrentWeatherByLatLon(
                appId,
                lat,
                lon
            )
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        currentWeather.value = response.body()
                    } else {
                        errorString.value = Errors.SERVER_ERROR.error
                    }
                } catch (e: HttpException) {
                    errorString.value = e.message()
                } catch (e: Throwable) {
                    errorString.value = Errors.UNKNOWN.error
                }
            }
        }
    }

    fun getHourlyForecastByLatLon(
        appId: String,
        lat: String,
        lon: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = remoteDataSource.getHourlyForecastByLatLon(
                appId,
                lat,
                lon
            )
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        hourlyForecast.value = response.body()
                        response.body()?.let { filterDailyForecasts(it) }
                    } else {
                        errorString.value = Errors.SERVER_ERROR.error
                    }
                } catch (e: HttpException) {
                    errorString.value = e.message()
                } catch (e: Throwable) {
                    errorString.value = Errors.UNKNOWN.error
                }
            }
        }
    }


    fun getCurrentWeatherByCityName(
        appId: String,
        cityName: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = remoteDataSource.getCurrentWeatherByCityName(
                appId,
                cityName
            )
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        currentWeather.value = response.body()
                    } else {
                        errorString.value = Errors.SERVER_ERROR.error
                    }
                } catch (e: HttpException) {
                    errorString.value = e.message()
                } catch (e: Throwable) {
                    errorString.value = Errors.UNKNOWN.error
                }
            }
        }
    }

    fun getHourlyForecastByCityName(
        appId: String,
        cityName: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = remoteDataSource.getHourlyForecastByCityName(
                appId,
                cityName
            )
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        hourlyForecast.value = response.body()
                        response.body()?.let { filterDailyForecasts(it) }
                    } else {
                        errorString.value = Errors.SERVER_ERROR.error
                    }
                } catch (e: HttpException) {
                    errorString.value = e.message()
                } catch (e: Throwable) {
                    errorString.value = Errors.UNKNOWN.error
                }
            }
        }
    }

    private fun filterDailyForecasts(hourlyEntity: HourlyEntity) {
        val newList = mutableListOf<HourlyListEntity>()

        var previousDay: String = hourlyEntity.list[0].dt_txt.substring(8..9)

        hourlyEntity.list.forEach { hourlyListEntity ->
            val newDay = hourlyListEntity.dt_txt.substring(8..9)
            if (newDay != previousDay) {

                newList.add(hourlyListEntity)
                previousDay = newDay
            }

        }

        dailyForecast.value = HourlyEntity(
            hourlyEntity.cod,
            hourlyEntity.message,
            hourlyEntity.cnt,
            newList
        )

    }


}