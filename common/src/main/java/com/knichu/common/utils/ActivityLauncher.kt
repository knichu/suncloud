package com.knichu.common.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.knichu.common.constants.UrlConstants

object ActivityLauncher {
    fun startCitySearchActivity(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(UrlConstants.CITY_SEARCH_URL))
        context.startActivity(intent)
    }
}