package com.knichu.common_ui.databinding

import android.content.res.ColorStateList
import android.graphics.Color
import com.knichu.common_ui.enums.WeatherIcon
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.knichu.common_ui.R
import com.knichu.common_ui.enums.WindDirectionIcon
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@BindingAdapter("visibleOrGone")
fun View.bindVisibleOrGone(visible: Boolean?) {
    this.visibility = if (visible == true) View.VISIBLE else View.GONE
}

@BindingAdapter("visibleOrGoneByNull")
fun View.bindVisibleOrGoneByNull(visible: Any?) {
    this.visibility = if (visible == null) View.VISIBLE else View.GONE
}

@BindingAdapter("weatherIcon")
fun ImageView.setWeatherIcon(weatherCondition: String?) {
    this.setImageResource(WeatherIcon.getIconImage(weatherCondition).icon)
}

@BindingAdapter("windDirectionIcon")
fun ImageView.setWindDirectionIcon(windDirection: String?) {
    this.setImageResource(WindDirectionIcon.getIconImage(windDirection).icon)
}

@BindingAdapter("visibleOrGoneOld")
fun ImageView.bindVisibleOrGoneOld(visible: Boolean?) {
    this.visibility = if (visible == true) View.VISIBLE else View.GONE
}

@BindingAdapter("visibleOrGone")
fun ImageView.bindVisibleOrGone(visible: Boolean?) {
    if (visible == true && visibility == View.GONE) {
        alpha = 0f
        visibility = View.VISIBLE
        animate().alpha(1f)
    } else if (visible == false && visibility == View.VISIBLE) {
        animate().alpha(0f).withEndAction { visibility = View.GONE }
    }
}

@BindingAdapter("timeWithAmPm")
fun TextView.setTimeWithAmPm(timeString: String?) {
    try {
        val parser = SimpleDateFormat("HHmm", Locale("ko", "KR"))
        val time: Date = parser.parse(timeString ?: "1200") ?: return
        val formatter = SimpleDateFormat("a h", Locale("ko", "KR"))
        val formattedTime: String = formatter.format(time) + "시"
        this.text = formattedTime
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("timeWithAmPmDetail")
fun TextView.setTimeWithAmPmDetail(timeString: String?) {
    try {
        val parser = SimpleDateFormat("HHmm", Locale("ko", "KR"))
        val time: Date = parser.parse(timeString ?: "1200") ?: return
        val formatter = SimpleDateFormat("a h:mm", Locale("ko", "KR"))
        val formattedTime: String = formatter.format(time)
        this.text = formattedTime
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("temperatureText")
fun TextView.setTemperatureText(temperatureString: String?) {
    try {
        if (temperatureString == null) {
            this.text = "준비중"
        } else {
            val parser = "$temperatureString°"
            this.text = parser
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("percentageText")
fun TextView.setPercentageText(percentageString: String?) {
    try {
        if (percentageString == null) {
            this.text = "준비중"
        } else {
            val parser = "$percentageString%"
            this.text = parser
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("windSpeedText")
fun TextView.setWindSpeedText(windSpeedString: String?) {
    try {
        if (windSpeedString == null) {
            this.text = "준비중"
        } else {
            val parser = "$windSpeedString m/s"
            this.text = parser
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("airQualityText")
fun TextView.setAirQualityText(airQualityString: String?) {
    try {
        if (airQualityString == null) {
            this.text = "준비중"
        } else {
            var parser : String? = null
            when (airQualityString) {
                "1" -> parser = "좋음"
                "2" -> parser = "보통"
                "3" -> parser = "나쁨"
                "4" -> parser = "매우 나쁨"
                "0" -> parser = "준비중"
            }
            this.text = parser
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("airDensityText")
fun TextView.setAirDensityText(airDensityString: String?) {
    try {
        if (airDensityString == null) {
            this.text = ""
        } else {
            val parser = "($airDensityString ㎍/㎥)"
            this.text = parser
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("airQualityLevel")
fun ProgressBar.setAirQualityLevel(airQualityLevel: String?) {
    try {
        if (airQualityLevel == null) {
            this.progress = 0
            this.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
        } else {
            val parser = airQualityLevel.toInt()
            this.progress = parser

            val color = when (parser) {
                1 -> ContextCompat.getColor(context, R.color.Cyan900)
                2 -> ContextCompat.getColor(context, R.color.Green900)
                3 -> ContextCompat.getColor(context, R.color.Orange900)
                4 -> ContextCompat.getColor(context, R.color.Red900)
                else -> ContextCompat.getColor(context, R.color.white)
            }
            this.progressTintList = ColorStateList.valueOf(color)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}