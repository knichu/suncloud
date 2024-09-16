package com.knichu.forecast.ui.citysearch

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.knichu.common_ui.base.BaseActivity
import com.knichu.forecast.R
import com.knichu.forecast.databinding.ActivityCitySearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CitySearchActivity : BaseActivity<ActivityCitySearchBinding>(
    R.layout.activity_city_search
) {
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.container) as? NavHostFragment)?.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navController?.setGraph(R.navigation.nav_city_search)
    }
}