package com.knichu.domain.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DateTimeParser {

    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault())
        val calendar = Calendar.getInstance()
        return dateFormat.format(calendar.time)
    }

    fun adjustLiveWeatherTime(): String {
        val currentTime = getCurrentDateTime()
        val dateFormat = SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.time = dateFormat.parse(currentTime)!!
        val minutes = calendar.get(Calendar.MINUTE)
        if (minutes <= 40) {
            calendar.add(Calendar.HOUR_OF_DAY, -1)
            calendar.set(Calendar.MINUTE, 0)
        } else {
            calendar.set(Calendar.MINUTE, 0)
        }
        return dateFormat.format(calendar.time)
    }

    fun adjustShortWeatherTime(): String {
        val currentTime = getCurrentDateTime()
        val dateFormat = SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.time = dateFormat.parse(currentTime)!!
        val currentHourMinute = SimpleDateFormat("HHmm", Locale.getDefault()).format(calendar.time)
        val apiTimes = listOf("0210", "0510", "0810", "1110", "1410", "1710", "2010", "2310")
        for (apiTime in apiTimes) {
            if (currentHourMinute <= apiTime) {
                return if (apiTime == apiTimes.first()) {
                    calendar.add(Calendar.DAY_OF_YEAR, -1)
                    calendar.set(Calendar.HOUR, 23)
                    calendar.set(Calendar.MINUTE, 0)
                    dateFormat.format(calendar.time)
                } else {
                    calendar.set(Calendar.HOUR, apiTime.substring(0, 2).toInt() - 3)
                    calendar.set(Calendar.MINUTE, 0)
                    dateFormat.format(calendar.time)
                }
            }
        }
        calendar.set(Calendar.HOUR, 23)
        calendar.set(Calendar.MINUTE, 0)
        return dateFormat.format(calendar.time)
    }

    fun adjustLongTermForecastTime(): String {
        val currentTime = getCurrentDateTime()
        val dateFormat = SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.time = dateFormat.parse(currentTime)!!
        val currentHourMinute = SimpleDateFormat("HHmm", Locale.getDefault()).format(calendar.time)
        val apiTimes = listOf("0600", "1800")
        for (apiTime in apiTimes) {
            if (currentHourMinute <= apiTime) {
                return if (apiTime == apiTimes.first()) {
                    calendar.add(Calendar.DAY_OF_YEAR, -1)
                    calendar.set(Calendar.HOUR, 18)
                    calendar.set(Calendar.MINUTE, 0)
                    dateFormat.format(calendar.time)
                } else {
                    calendar.set(Calendar.HOUR, 6)
                    calendar.set(Calendar.MINUTE, 0)
                    dateFormat.format(calendar.time)
                }
            }
        }
        calendar.set(Calendar.HOUR, 18)
        calendar.set(Calendar.MINUTE, 0)
        return dateFormat.format(calendar.time)
    }

}