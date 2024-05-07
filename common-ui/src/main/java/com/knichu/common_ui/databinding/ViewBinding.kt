package com.knichu.common_ui.databinding

import com.knichu.common_ui.enums.WeatherIcon
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.appbar.AppBarLayout
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
fun TextView.setTemperatureText(timeString: String?) {
    try {
        if (timeString == null) {
            this.text = "준비중"
        } else {
            val parser = "$timeString°"
            this.text = parser
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("percentageText")
fun TextView.setPercentageText(timeString: String?) {
    try {
        val parser = "$timeString%"
        this.text = parser
    } catch (e: Exception) {
        e.printStackTrace()
    }
}