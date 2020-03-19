package abolfazli.mahdi.weather.weather.data.entities

data class WeatherEntity(
    val id: Int,
    val main: String,
    val description: String,
    var icon: String
)