package com.knichu.nationwide.ui

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.knichu.nationwide.model.CityInfo
import com.knichu.nationwide.model.ZOOM_TIER1_ONLY

private const val KOREA_CENTER_LAT = 36.5
private const val KOREA_CENTER_LON = 127.8
private const val INITIAL_ZOOM = 6.3

@Composable
fun NationwideScreen(viewModel: NationwideViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    NationwideMapView(
        uiState = uiState,
        onInitialLoad = { viewModel.handleIntent(NationwideUiIntent.LoadInitialData) },
        onCameraIdle = { zoom -> viewModel.handleIntent(NationwideUiIntent.OnCameraIdle(zoom)) },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun NationwideMapView(
    uiState: NationwideUiState,
    onInitialLoad: () -> Unit,
    onCameraIdle: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val naverMapRef = remember { mutableStateOf<NaverMap?>(null) }
    val markersRef = remember { mutableMapOf<String, Marker>() }
    val currentZoom = remember { mutableStateOf(INITIAL_ZOOM) }

    AndroidView(
        factory = { ctx ->
            MapView(ctx).apply {
                lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                    override fun onCreate(owner: LifecycleOwner) = this@apply.onCreate(null)
                    override fun onStart(owner: LifecycleOwner) = this@apply.onStart()
                    override fun onResume(owner: LifecycleOwner) = this@apply.onResume()
                    override fun onPause(owner: LifecycleOwner) = this@apply.onPause()
                    override fun onStop(owner: LifecycleOwner) = this@apply.onStop()
                    override fun onDestroy(owner: LifecycleOwner) = this@apply.onDestroy()
                })

                getMapAsync { map ->
                    naverMapRef.value = map
                    setupInitialCamera(map)
                    map.addOnCameraIdleListener {
                        currentZoom.value = map.cameraPosition.zoom
                        onCameraIdle(map.cameraPosition.zoom)
                    }
                    onInitialLoad()
                }
            }
        },
        modifier = modifier
    )

    // zoom 변화 또는 날씨 데이터 변화 시 마커 업데이트
    val map = naverMapRef.value
    LaunchedEffect(map, uiState.cityWeatherMap, uiState.allCities, currentZoom.value) {
        if (map == null) return@LaunchedEffect
        updateMarkers(context, map, uiState, markersRef)
    }
}

private val KOREA_BOUNDS = LatLngBounds(
    LatLng(33.0, 124.5),  // 남서 (제주 남단 / 서해)
    LatLng(38.9, 132.0)   // 북동 (휴전선 근처 / 독도)
)
private const val MIN_ZOOM = 5.5

private fun setupInitialCamera(map: NaverMap) {
    map.extent = KOREA_BOUNDS
    map.minZoom = MIN_ZOOM
    map.cameraPosition = CameraPosition(LatLng(KOREA_CENTER_LAT, KOREA_CENTER_LON), INITIAL_ZOOM)
    with(map.uiSettings) {
        isZoomControlEnabled = false
        isCompassEnabled = false
        isScaleBarEnabled = false
    }
}

private fun updateMarkers(
    context: Context,
    map: NaverMap,
    uiState: NationwideUiState,
    markers: MutableMap<String, Marker>
) {
    val zoom = map.cameraPosition.zoom
    val maxTier = if (zoom <= ZOOM_TIER1_ONLY) 1 else 2
    val visibleCityNames = uiState.allCities
        .filter { it.tier <= maxTier }
        .map { it.name }
        .toSet()

    // 범위 밖 마커 제거
    val iter = markers.iterator()
    while (iter.hasNext()) {
        val entry = iter.next()
        if (entry.key !in visibleCityNames) {
            entry.value.map = null
            iter.remove()
        }
    }

    // 날씨 데이터가 준비된 도시 마커 추가/업데이트
    uiState.cityWeatherMap.forEach { (cityName, weather) ->
        if (cityName !in visibleCityNames) return@forEach
        if (weather.isLoading || weather.temperature == null) return@forEach

        val cityInfo = uiState.allCities.find { it.name == cityName } ?: return@forEach
        val icon = MarkerBitmapFactory.create(context, weather.weatherCondition, weather.temperature)

        val existing = markers[cityName]
        if (existing != null) {
            existing.icon = icon
            existing.captionText = cityName
        } else {
            markers[cityName] = Marker().apply {
                position = LatLng(cityInfo.lat, cityInfo.lon)
                this.icon = icon
                captionText = cityName
                captionTextSize = 12f
                this.map = map
            }
        }
    }
}
