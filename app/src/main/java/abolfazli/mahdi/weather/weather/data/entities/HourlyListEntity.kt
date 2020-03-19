package abolfazli.mahdi.weather.weather.data.entities

data class HourlyListEntity(
    val dt: Int,
    val dt_txt: String,
    val main: WeatherMainEntity,
    val weather: List<WeatherEntity>
) {

}
