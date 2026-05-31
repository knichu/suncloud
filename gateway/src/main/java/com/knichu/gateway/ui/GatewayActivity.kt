package com.knichu.gateway.ui

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.knichu.common.base.SplashReadyCallback
import com.knichu.common_ui.base.BaseActivity
import com.knichu.forecast.ui.ForecastHostFragment
import com.knichu.gateway.R
import com.knichu.gateway.databinding.ActivityGatewayBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GatewayActivity : BaseActivity<ActivityGatewayBinding>(
    ActivityGatewayBinding::inflate
), SplashReadyCallback {

    private var isDataReady = false

    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host) as? NavHostFragment)?.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { !isDataReady }

        // GPS 거부 등 데이터가 끝내 안 오는 경우 대비 안전망
        lifecycleScope.launch {
            delay(5_000)
            isDataReady = true
        }

        with(viewBinding) {
            with(bottomNavigationView) {
                if (navController != null) setupWithNavController(this, navController!!)

                setOnItemReselectedListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.nav_forecast -> scrollToTopForecast()
                    }
                }
            }
        }
    }

    override fun onDataReady() {
        isDataReady = true
    }

    private fun scrollToTopForecast() {
        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host)
        if (fragment is NavHostFragment) {
            fragment.childFragmentManager
                .fragments
                .filterIsInstance<ForecastHostFragment>()
                .firstOrNull()
                ?.scrollToTop()
        }
    }
}
