package abolfazli.mahdi.weather.weather.data.entities

data class HourlyEntity(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<HourlyListEntity>
)