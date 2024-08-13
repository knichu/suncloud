package com.knichu.forecast.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import com.knichu.forecast.BR
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.appbar.AppBarLayout
import com.knichu.common_ui.adapter.Weather24HourListAdapter
import com.knichu.common_ui.adapter.WeatherWeeklyListAdapter
import com.knichu.common_ui.base.BaseViewModelFragment
import com.knichu.domain.vo.Weather24HourItemVO
import com.knichu.domain.vo.WeatherWeeklyItemVO
import com.knichu.forecast.R
import com.knichu.forecast.databinding.FragmentForecastBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ForecastFragment: BaseViewModelFragment<FragmentForecastBinding, ForecastViewModel>(
    R.layout.fragment_forecast,
    BR.viewModel
) {

    override val viewModel: ForecastViewModel by viewModels()

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // 권한이 부여된 경우
            fetchCurrentLocation()
        } else {
            // 권한이 거부된 경우 처리
            // 예를 들어 사용자에게 권한이 필요하다는 설명을 보여줄 수 있음
        }
    }

    private val offsetChangeListener = AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
        viewDataBinding.pullToRefreshLayout.isEnabled = verticalOffset == 0
        viewModel.setAppBarExpanded(verticalOffset == 0) // liveWeather 이미지 유지 결정
    }

    private val weather24HourListAdapter = Weather24HourListAdapter().apply {
        listener = object : Weather24HourListAdapter.Listener {
            override fun onItemClick(item: Weather24HourItemVO) {

            }
        }
    }

    private val weatherWeeklyListAdapter = WeatherWeeklyListAdapter().apply {
        listener = object : WeatherWeeklyListAdapter.Listener {
            override fun onItemClick(item: WeatherWeeklyItemVO) {

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        with(viewDataBinding) {

            appBar.addOnOffsetChangedListener(offsetChangeListener)

            with(includeLayoutWeather24hour.weather24hourRecyclerView) {
                adapter = weather24HourListAdapter
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }

            with(includeLayoutWeatherWeekly.weatherWeeklyRecyclerView) {
                adapter = weatherWeeklyListAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }

            cityIconButton.setOnClickListener {
                if (leftDrawer.isDrawerOpen(GravityCompat.START)) {
                    leftDrawer.closeDrawer(GravityCompat.START)
                } else {
                    leftDrawer.openDrawer(GravityCompat.START)
                }
            }
        }

        with(viewModel) {

            isRefreshing.observe {
                if (it) {
                    checkLocationPermission()
                    setRefreshing(false)
                }
            }

            weather24HourData.observe {
                weather24HourListAdapter.submitList(it)
            }

            weatherWeeklyData.observe {
                weatherWeeklyListAdapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        viewDataBinding.appBar.removeOnOffsetChangedListener(offsetChangeListener)
        viewDataBinding.includeLayoutWeather24hour.weather24hourRecyclerView.adapter = null
        viewDataBinding.includeLayoutWeatherWeekly.weatherWeeklyRecyclerView.adapter = null
        super.onDestroyView()
    }

    fun scrollToTop() {
        viewDataBinding.appBar.setExpanded(true, true)
        viewDataBinding.secondView.smoothScrollTo(0, 0)
    }

    private fun initView() {
        checkLocationPermission()
        checkAppBar()
    }

    private fun checkAppBar() {
        when (viewModel.isAppBarExpanded.value) {
            true -> viewDataBinding.appBar.setExpanded(true, false)
            false -> viewDataBinding.appBar.setExpanded(false, false)
            else -> viewDataBinding.appBar.setExpanded(true, false)
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fetchCurrentLocation()
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun fetchCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        val lon = it.longitude
                        val lat = it.latitude
                        viewModel.updateLocationFetchData(lon, lat)
                    } ?: Log.e(TAG, "마지막 위치가 null 입니다.")
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "위치 정보를 가져오는데 실패했습니다: ${e.message}")
                }
        } else {
            requestLocationPermission()
        }
    }

    companion object {
        private const val TAG = "ForecastFragment"
    }

}