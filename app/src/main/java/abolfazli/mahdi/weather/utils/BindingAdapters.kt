package abolfazli.mahdi.weather.utils

import abolfazli.mahdi.weather.R
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import kotlin.math.roundToInt


@BindingAdapter("icon")
fun loadImage(view: ImageView, imageUrl: String?) {
    Glide.with(view.context)
        .load(imageUrl)
        .into(view)
}

@BindingAdapter("showDegree")
fun showDegreeAndDropDecimals(view: TextView, degree: Double) {
    val result = degree.roundToInt().toString()
    view.text = view.context.getString(R.string.show_degree, result)

}

@BindingAdapter("showHumidity")
fun addPercentAndDecimal(view: TextView, humidity: Int) {
    view.text = view.context.getString(R.string.show_humidity, humidity)
}


@BindingAdapter("showDate")
fun showDate(view: TextView, date: String?) {
    if (date != null && date.length > 10)
        view.text = date.substring(8..9)
}

