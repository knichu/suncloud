package com.knichu.gateway.ui

import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.knichu.common_ui.base.BaseActivity
import com.knichu.forecast.ui.ForecastFragment
import com.knichu.gateway.R
import com.knichu.gateway.databinding.ActivityGatewayBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GatewayActivity : BaseActivity<ActivityGatewayBinding>(
    R.layout.activity_gateway,
) {

    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host) as? NavHostFragment)?.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(viewDataBinding) {

            with(bottomNavigationView) {
                if (navController != null) setupWithNavController(this, navController!!)

                setOnItemReselectedListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.nav_forecast -> {
                        }

                        R.id.nav_nationwide -> {

                        }
                    }
                }
            }
        }
    }


