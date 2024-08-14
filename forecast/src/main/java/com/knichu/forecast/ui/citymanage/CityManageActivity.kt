package com.knichu.forecast.ui.citymanage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.knichu.common_ui.base.BaseActivity
import com.knichu.forecast.R
import com.knichu.forecast.databinding.ActivityCityManageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CityManageActivity : BaseActivity<ActivityCityManageBinding>(
    R.layout.activity_city_manage
) {
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.container) as? NavHostFragment)?.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navController?.setGraph(R.navigation.nav_city_manage)
    }
}