package abolfazli.mahdi.weather.di

import abolfazli.mahdi.weather.MainActivity
import abolfazli.mahdi.weather.weather.ui.SearchFragment
import abolfazli.mahdi.weather.weather.ui.WeatherFragment
import abolfazli.mahdi.weather.weather.ui.WeatherModule
import android.app.Application
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
AppModule::class,
WeatherModule::class
])
interface AppComponent{

    fun inject(app: Application)

    fun inject(target: MainActivity)

    fun inject(target: WeatherFragment)

    fun inject(target: SearchFragment)

}