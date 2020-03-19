package abolfazli.mahdi.weather.weather.ui

import abolfazli.mahdi.weather.weather.data.WeatherRemoteDataSource
import abolfazli.mahdi.weather.weather.data.WeatherService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class WeatherModule {

    @Singleton
    @Provides
    fun provideWeatherService(retrofit: Retrofit): WeatherService {
        return retrofit.create(WeatherService::class.java)
    }



    @Provides
    fun provideRemoteDataSource(weatherService: WeatherService): WeatherRemoteDataSource {
        return WeatherRemoteDataSource(weatherService)
    }
}