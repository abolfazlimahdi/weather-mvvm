package abolfazli.mahdi.weather.weather.data


import abolfazli.mahdi.weather.weather.data.entities.*
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.testcoroutinesrule.TestCoroutineRule
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(MockitoJUnitRunner::class)
class WeatherRemoteDataSourceTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private var mockWebServer = MockWebServer()

    @Mock
    private lateinit var weatherService: WeatherService
    private lateinit var secondWeatherService: WeatherService

    @InjectMocks
    private lateinit var remoteDataSource: WeatherRemoteDataSource


    @Before
    fun setup() {
        secondWeatherService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()
            .create(WeatherService::class.java)
    }


    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Remote get current weather by lat lon`() = runBlocking {

        mockWebServer.enqueue(
            MockResponse()
                .setBody(TestUtil.getJson("current_response.json"))
                .setResponseCode(200)
        )


        whenever(weatherService.fetchCurrentWeatherByLatLon(anyString(), anyString(), anyString()))
            .doReturn(
                secondWeatherService.fetchCurrentWeatherByLatLon(
                    "appId",
                    "lat",
                    "lon"
                )
            )

        val testObserver = remoteDataSource.getCurrentWeatherByLatLon(
            "appId",
            "lat",
            "lon"
        )
        val response = Response.success(
            CurrentWeatherEntity(
                WeatherMainEntity(280.32, 1012, 81, 279.15, 281.15),
                listOf<WeatherEntity>(WeatherEntity(300, "Drizzle", "light intensity drizzle", "09d")), "London"
            )
        )

        assertEquals(testObserver.body(), response.body())

    }


    @ExperimentalCoroutinesApi
    @Test
    fun `Remote get current weather by city name`() = runBlocking {


        mockWebServer.enqueue(
            MockResponse()
                .setBody(TestUtil.getJson("current_response.json"))
                .setResponseCode(200)
        )



        whenever(weatherService.fetchCurrentWeatherByCityName(anyString(), anyString()))
            .doReturn(
                secondWeatherService.fetchCurrentWeatherByCityName(
                    "appId",
                    "cityName"
                )
            )

        val testObserver = remoteDataSource.getCurrentWeatherByCityName(
            "appId",
            "cityName"
        )
        val response = Response.success(
            CurrentWeatherEntity(
                WeatherMainEntity(280.32, 1012, 81, 279.15, 281.15),
                listOf<WeatherEntity>(WeatherEntity(300, "Drizzle", "light intensity drizzle", "09d")), "London"
            )
        )

        assertEquals(testObserver.body(), response.body())

    }


    @ExperimentalCoroutinesApi
    @Test
    fun `Remote get hourly forecast by lat lon`() = runBlocking {


        mockWebServer.enqueue(
            MockResponse()
                .setBody(TestUtil.getJson("hourly_response.json"))
                .setResponseCode(200)
        )



        whenever(weatherService.fetchHourlyForecastByLatLong(anyString(), anyString(), anyString()))
            .doReturn(
                secondWeatherService.fetchHourlyForecastByLatLong(
                    "appId",
                    "lat",
                    "lon"
                )
            )

        val testObserver = remoteDataSource.getHourlyForecastByLatLon(
            "appId",
            "lat",
            "lon"
        )
        val response = Response.success(
            HourlyEntity(
                "200", 0, 1,
                listOf<HourlyListEntity>(
                    HourlyListEntity(
                        1585526400,
                        "2020-03-30 00:00:00",
                        WeatherMainEntity(1.9, 1039, 77, 1.9, 2.55),
                        listOf<WeatherEntity>(WeatherEntity(801, "Clouds", "few clouds", "02n"))
                    )
                )
            )
        )

        assertEquals(testObserver.body(), response.body())

    }


    @ExperimentalCoroutinesApi
    @Test
    fun `Remote get hourly forecast by city name`() = runBlocking {


        mockWebServer.enqueue(
            MockResponse()
                .setBody(TestUtil.getJson("hourly_response.json"))
                .setResponseCode(200)
        )



        whenever(weatherService.fetchHourlyForecastByCityName(anyString(), anyString()))
            .doReturn(
                secondWeatherService.fetchHourlyForecastByCityName(
                    "appId",
                    "cityname"
                )
            )

        val testObserver = remoteDataSource.getHourlyForecastByCityName(
            "appId",
            "cityName"
        )
        val response = Response.success(
            HourlyEntity(
                "200", 0, 1,
                listOf<HourlyListEntity>(
                    HourlyListEntity(
                        1585526400,
                        "2020-03-30 00:00:00",
                        WeatherMainEntity(1.9, 1039, 77, 1.9, 2.55),
                        listOf<WeatherEntity>(WeatherEntity(801, "Clouds", "few clouds", "02n"))
                    )
                )
            )
        )

        assertEquals(testObserver.body(), response.body())

    }


    private fun <T> verifyBlocking(mock: T, f: suspend T.() -> Unit) {
        val m = Mockito.verify(mock)
        runBlocking { m.f() }
    }

}