package abolfazli.mahdi.weather.weather.data.entities

data class WeatherMainEntity(
    val temp: Double,
    val pressure: Int,
    val humidity: Int,
    val temp_min: Double,
    val temp_max: Double
) {
}