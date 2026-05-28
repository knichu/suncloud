package com.knichu.forecast.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.knichu.forecast.ui.citymanage.CityManageScreen
import com.knichu.forecast.ui.citymanage.CityManageViewModel
import com.knichu.forecast.ui.citysearch.CitySearchScreen
import com.knichu.forecast.ui.citysearch.CitySearchViewModel
import com.knichu.forecast.ui.forecast.ForecastScreen
import com.knichu.forecast.ui.forecast.ForecastUiEffect
import com.knichu.forecast.ui.forecast.ForecastViewModel

private fun Context.findActivity(): Activity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    return null
}

sealed class ForecastRoute(val route: String) {
    object Forecast   : ForecastRoute("forecast")
    object CityManage : ForecastRoute("city_manage")
    object CitySearch : ForecastRoute("city_search")
}

@Composable
fun ForecastNavHost(
    forecastViewModel: ForecastViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val context = LocalContext.current

    // ForecastViewModel effect는 여기서 단일 수집 (네비게이션 + 앱 종료)
    LaunchedEffect(Unit) {
        forecastViewModel.uiEffect.collect { effect ->
            when (effect) {
                is ForecastUiEffect.NavigateToCitySearch ->
                    navController.navigate(ForecastRoute.CitySearch.route)
                is ForecastUiEffect.NavigateToCityManage ->
                    navController.navigate(ForecastRoute.CityManage.route)
                is ForecastUiEffect.ExitApp ->
                    context.findActivity()?.finish()
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = ForecastRoute.Forecast.route,
        modifier = modifier
    ) {
        composable(ForecastRoute.Forecast.route) {
            ForecastScreen(viewModel = forecastViewModel)
        }
        composable(ForecastRoute.CityManage.route) {
            val cityManageViewModel: CityManageViewModel = hiltViewModel()
            CityManageScreen(
                viewModel = cityManageViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(ForecastRoute.CitySearch.route) {
            val citySearchViewModel: CitySearchViewModel = hiltViewModel()
            CitySearchScreen(
                viewModel = citySearchViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
