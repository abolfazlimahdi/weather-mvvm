package abolfazli.mahdi.weather.weather.data.entities

data class CurrentWeatherEntity(
    val main: WeatherMainEntity,
    val weather: List<WeatherEntity>,
    val name: String
) {
}

