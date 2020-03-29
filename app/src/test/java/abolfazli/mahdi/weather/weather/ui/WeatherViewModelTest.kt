package abolfazli.mahdi.weather.weather.ui

import abolfazli.mahdi.weather.OneTimeObserver
import abolfazli.mahdi.weather.weather.data.WeatherRemoteDataSource
import abolfazli.mahdi.weather.weather.data.entities.*
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.github.testcoroutinesrule.TestCoroutineRule
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.whenever
import kotlinx.coroutines.*
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner.Silent::class)
class WeatherViewModelTest {

    private lateinit var weatherViewModel: WeatherViewModel


    @kotlinx.coroutines.ObsoleteCoroutinesApi
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var remoteDataSource: WeatherRemoteDataSource

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        weatherViewModel = WeatherViewModel(remoteDataSource)
    }

    @Test
    fun testGetCurrentWeatherByLatLon() = runBlocking {
        weatherViewModel.getCurrentWeatherByLatLon("appId", "lat", "lon")

        val response = Response.success(
            CurrentWeatherEntity(
                WeatherMainEntity(10.0, 200, 10, 12.0, 13.0),
                listOf<WeatherEntity>(WeatherEntity(10, "main", "description", "icon")), "name"
            )
        )

        whenever(
            remoteDataSource.getCurrentWeatherByLatLon(
                anyString(),
                anyString(),
                anyString()
            )
        ).doReturn(response)

        weatherViewModel.currentWeather.observeOnce {

            assertEquals(response.body(), it)
        }
    }

    @Test
    fun testGetCurrentWeatherByCityName() = runBlocking {
        weatherViewModel.getCurrentWeatherByCityName("appId", "cityName")

        val response = Response.success(
            CurrentWeatherEntity(
                WeatherMainEntity(10.0, 200, 10, 12.0, 13.0),
                listOf<WeatherEntity>(WeatherEntity(10, "main", "description", "icon")), "name"
            )
        )

        whenever(
            remoteDataSource.getCurrentWeatherByCityName(
                anyString(),
                anyString()
            )
        ).doReturn(response)

        weatherViewModel.currentWeather.observeOnce {

            assertEquals(response.body(), it)
        }
    }

    @Test
    fun testGetHourlyForecastByLatLon() = runBlocking {
        weatherViewModel.getHourlyForecastByLatLon("appId", "lat", "lon")


        val response = Response.success(
            HourlyEntity(
                "200", 0, 1,
                listOf<HourlyListEntity>(
                    HourlyListEntity(
                        1585526400,
                        "",
                        WeatherMainEntity(1.9, 1039, 77, 1.9, 2.55),
                        listOf<WeatherEntity>(WeatherEntity(801, "Clouds", "few clouds", "02n"))
                    )
                )
            )
        )


        whenever(
            remoteDataSource.getHourlyForecastByLatLon(
                anyString(),
                anyString(),
                anyString()
            )
        ).doReturn(response)

        weatherViewModel.hourlyForecast.observeOnce {

            assertEquals(response.body(), it)
        }
    }

    @Test
    fun testGetHourlyForecastByCityName() = runBlocking {
        weatherViewModel.getHourlyForecastByCityName("appId", "cityName")

        val response = Response.success(
            HourlyEntity(
                "cod", 0, 0,
                listOf<HourlyListEntity>(
                    HourlyListEntity(
                        0,
                        "",
                        WeatherMainEntity(10.0, 200, 10, 12.0, 13.0),
                        listOf<WeatherEntity>(WeatherEntity(10, "main", "description", "icon"))
                    )
                )
            )
        )

        whenever(
            remoteDataSource.getHourlyForecastByCityName(
                anyString(),
                anyString()
            )
        ).doReturn(response)

        weatherViewModel.hourlyForecast.observeOnce {

            assertEquals(response.body(), it)
        }
    }

    private fun <T> LiveData<T>.observeOnce(onChangeHandler: (T) -> Unit) {
        val observer = OneTimeObserver(handler = onChangeHandler)
        observe(observer, observer)
    }
}