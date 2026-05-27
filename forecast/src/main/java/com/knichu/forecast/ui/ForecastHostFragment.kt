package com.knichu.forecast.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.location.LocationServices
import com.knichu.forecast.ui.forecast.ForecastUiIntent
import com.knichu.forecast.ui.forecast.ForecastViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForecastHostFragment : Fragment() {

    val viewModel: ForecastViewModel by viewModels()

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) fetchCurrentLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    ForecastNavHost(forecastViewModel = viewModel)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkLocationPermission()
    }

    fun scrollToTop() {
        viewModel.handleIntent(ForecastUiIntent.ScrollToTop)
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fetchCurrentLocation()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun fetchCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    viewModel.handleIntent(
                        ForecastUiIntent.UpdateLocation(it.longitude, it.latitude)
                    )
                } ?: Log.e(TAG, "마지막 위치가 null 입니다.")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "위치 정보를 가져오는데 실패했습니다: ${e.message}")
            }
    }

    companion object {
        private const val TAG = "ForecastHostFragment"
    }
}
