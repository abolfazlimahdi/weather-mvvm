package abolfazli.mahdi.weather

import abolfazli.mahdi.weather.di.AppComponent
import abolfazli.mahdi.weather.di.AppModule
import abolfazli.mahdi.weather.di.DaggerAppComponent
import android.app.Application

class WeatherApp: Application() {


    lateinit var weatherComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        weatherComponent = initDagger(this)

    }

    private fun initDagger(app: WeatherApp): AppComponent =
        DaggerAppComponent.builder()
            .appModule(AppModule(app))
            .build()


}