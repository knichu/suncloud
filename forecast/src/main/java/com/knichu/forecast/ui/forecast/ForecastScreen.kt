package com.knichu.forecast.ui.forecast

import android.content.Context
import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.knichu.common_ui.R
import com.knichu.common_ui.enums.WeatherIcon
import com.knichu.common_ui.enums.WindDirectionIcon
import com.knichu.domain.constants.WeatherTempUnit
import com.knichu.domain.util.TemperatureParser
import com.knichu.domain.vo.AirPollutionDataVO
import com.knichu.domain.vo.SunriseSunsetVO
import com.knichu.domain.vo.Weather24HourItemVO
import com.knichu.domain.vo.WeatherForecastTextVO
import com.knichu.domain.vo.WeatherNowCityListItemVO
import com.knichu.domain.vo.WeatherNowVO
import com.knichu.domain.vo.WeatherOtherInfoVO
import com.knichu.domain.vo.WeatherWeeklyItemVO
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

private val BgColor = Color(0xFFEAEAEA)

// 벡터 드로어블을 비트맵으로 한 번만 렌더링해서 재사용 — 스크롤 복귀 시 재렌더링 방지
private val iconBitmapCache = HashMap<Pair<Int, Int>, ImageBitmap>()

private fun renderVectorToBitmap(context: Context, resId: Int, sizePx: Int): ImageBitmap {
    val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)
    val drawable = ContextCompat.getDrawable(context, resId)!!
    drawable.setBounds(0, 0, sizePx, sizePx)
    drawable.draw(canvas)
    return bitmap.asImageBitmap()
}

@Composable
private fun WeatherIconImage(weatherCondition: String?, size: Dp, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val resId = WeatherIcon.getIconImage(weatherCondition).icon
    val sizePx = with(LocalDensity.current) { size.toPx().toInt() }
    val bitmap = remember(resId, sizePx) {
        iconBitmapCache.getOrPut(Pair(resId, sizePx)) {
            renderVectorToBitmap(context, resId, sizePx)
        }
    }
    Image(bitmap = bitmap, contentDescription = null, modifier = modifier.size(size))
}

@Composable
private fun WindDirectionIconImage(windDirection: String?, size: Dp, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val resId = WindDirectionIcon.getIconImage(windDirection).icon
    val sizePx = with(LocalDensity.current) { size.toPx().toInt() }
    val bitmap = remember(resId, sizePx) {
        iconBitmapCache.getOrPut(Pair(resId, sizePx)) {
            renderVectorToBitmap(context, resId, sizePx)
        }
    }
    Image(bitmap = bitmap, contentDescription = null, modifier = modifier.size(size))
}

private fun formatTimeAmPm(timeString: String?): String {
    return try {
        val parser = SimpleDateFormat("HHmm", Locale("ko", "KR"))
        val time = parser.parse(timeString ?: "1200") ?: return ""
        val formatter = SimpleDateFormat("a h시", Locale("ko", "KR"))
        formatter.format(time)
    } catch (e: Exception) { "" }
}

private fun formatTimeAmPmDetail(timeString: String?): String {
    return try {
        val parser = SimpleDateFormat("HHmm", Locale("ko", "KR"))
        val time = parser.parse(timeString ?: "1200") ?: return ""
        val formatter = SimpleDateFormat("a h:mm", Locale("ko", "KR"))
        formatter.format(time)
    } catch (e: Exception) { "" }
}

private fun airQualityColor(quality: String?): Color = when (quality) {
    "1" -> Color(0xFF00CFCF)
    "2" -> Color(0xFF00C800)
    "3" -> Color(0xFFE69500)
    "4" -> Color(0xFFEC0000)
    else -> Color.LightGray
}

private fun airQualityText(quality: String?): String = when (quality) {
    "1" -> "좋음"
    "2" -> "보통"
    "3" -> "나쁨"
    "4" -> "매우 나쁨"
    else -> "준비중"
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ForecastScreen(
    viewModel: ForecastViewModel
) {
    val state by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(
        initialValue = if (state.isDrawerOpen) DrawerValue.Open else DrawerValue.Closed
    )
    val isScrolled by remember { derivedStateOf { scrollState.value > 400 } }
    val isRefreshing = state.isLoading && state.weatherNow != null
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.handleIntent(ForecastUiIntent.Refresh) }
    )

    var showExitDialog by remember { mutableStateOf(false) }
    var showCitySelectDialog by remember { mutableStateOf<WeatherNowCityListItemVO?>(null) }
    var showCurrentPositionDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.isDrawerOpen) {
        if (state.isDrawerOpen) drawerState.open() else drawerState.close()
    }
    LaunchedEffect(state.scrollToTopTrigger) {
        if (state.scrollToTopTrigger > 0L) scrollState.animateScrollTo(0)
    }

    BackHandler {
        if (drawerState.isOpen) {
            scope.launch { drawerState.close() }
            viewModel.handleIntent(ForecastUiIntent.CloseDrawer)
        } else {
            showExitDialog = true
        }
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("확인") },
            text = { Text("앱을 종료하시겠습니까?") },
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    viewModel.handleIntent(ForecastUiIntent.ExitApp)
                }) { Text("확인") }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) { Text("취소") }
            }
        )
    }

    showCitySelectDialog?.let { item ->
        AlertDialog(
            onDismissRequest = { showCitySelectDialog = null },
            title = { Text("확인") },
            text = { Text("이 도시를 선택하시겠습니까?") },
            confirmButton = {
                TextButton(onClick = {
                    item.city?.let { viewModel.handleIntent(ForecastUiIntent.SelectCity(it)) }
                    showCitySelectDialog = null
                }) { Text("확인") }
            },
            dismissButton = {
                TextButton(onClick = { showCitySelectDialog = null }) { Text("취소") }
            }
        )
    }

    if (showCurrentPositionDialog) {
        AlertDialog(
            onDismissRequest = { showCurrentPositionDialog = false },
            title = { Text("확인") },
            text = { Text("현 위치를 선택하시겠습니까?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.handleIntent(ForecastUiIntent.SelectCurrentPosition)
                    showCurrentPositionDialog = false
                }) { Text("확인") }
            },
            dismissButton = {
                TextButton(onClick = { showCurrentPositionDialog = false }) { Text("취소") }
            }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                ForecastDrawerContent(
                    state = state,
                    onCityClick = { showCitySelectDialog = it },
                    onCurrentPositionClick = { showCurrentPositionDialog = true },
                    onAddCityClick = {
                        scope.launch { drawerState.close() }
                        viewModel.handleIntent(ForecastUiIntent.NavigateToCitySearch)
                    },
                    onManageCityClick = {
                        scope.launch { drawerState.close() }
                        viewModel.handleIntent(ForecastUiIntent.NavigateToCityManage)
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        if (isScrolled) {
                            Text(
                                text = state.selectedCity ?: state.weatherNow?.city ?: "",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                            viewModel.handleIntent(ForecastUiIntent.OpenDrawer)
                        }) {
                            Icon(imageVector = Icons.Filled.LocationCity, contentDescription = "도시 목록")
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = BgColor)
                )
            },
            containerColor = BgColor
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .pullRefresh(pullRefreshState)
            ) {
                if (state.isLoading && state.weatherNow == null) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(bottom = 16.dp)
                    ) {
                        ForecastHeroSection(state = state)
                        if (state.weather24Hour.isNotEmpty()) {
                            Weather24HourCard(items = state.weather24Hour, tempUnit = state.tempUnit)
                        }
                        if (state.weatherWeekly.isNotEmpty()) {
                            WeatherWeeklyCard(items = state.weatherWeekly, tempUnit = state.tempUnit)
                        }
                        state.airPollution?.let { AirPollutionCard(data = it) }
                        state.sunriseSunset?.let { SunriseSunsetCard(data = it) }
                        state.weatherOtherInfo?.let { WeatherOtherInfoCard(data = it) }
                        state.weatherForecastText?.let { WeatherForecastTextCard(data = it) }
                    }
                }
                PullRefreshIndicator(
                    refreshing = isRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}

@Composable
private fun ForecastHeroSection(state: ForecastUiState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(BgColor)
            .padding(start = 24.dp, top = 8.dp, end = 10.dp, bottom = 8.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalArrangement = Arrangement.Center
        ) {
            if (state.selectedCity == null) {
                Text("현위치", fontSize = 14.sp, color = Color.Gray)
            }
            Text(
                text = state.selectedCity ?: state.weatherNow?.city ?: "-",
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "${TemperatureParser.convert(state.weatherNow?.temperature, state.tempUnit) ?: "-"}°",
                fontSize = 34.sp,
                color = Color.Gray
            )
        }
        WeatherIconImage(
            weatherCondition = state.weatherNow?.weatherCondition,
            size = 150.dp,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}

@Composable
private fun WeatherCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp)
            .padding(bottom = 6.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        content()
    }
}

@Composable
private fun Weather24HourCard(items: List<Weather24HourItemVO>, tempUnit: WeatherTempUnit) {
    WeatherCard {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 6.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items.forEach { item ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Text(formatTimeAmPm(item.time), fontSize = 10.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(6.dp))
                    WeatherIconImage(weatherCondition = item.weatherCondition, size = 32.dp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("${TemperatureParser.convert(item.temperature, tempUnit) ?: "-"}°", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_water_drop),
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Text("${item.rainProbability ?: "0"}%", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
private fun WeatherWeeklyCard(items: List<WeatherWeeklyItemVO>, tempUnit: WeatherTempUnit) {
    WeatherCard {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            items.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.dayOfTheWeek ?: "-",
                        fontSize = 16.sp,
                        modifier = Modifier.width(48.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.width(56.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_water_drop),
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Text("${item.rainProbability ?: "0"}%", fontSize = 14.sp, color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    WeatherIconImage(weatherCondition = item.weatherConditionAM, size = 24.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    WeatherIconImage(weatherCondition = item.weatherConditionPM, size = 24.dp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        "${TemperatureParser.convert(item.minTemperature, tempUnit) ?: "-"}°",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(36.dp)
                    )
                    Text(
                        "${TemperatureParser.convert(item.maxTemperature, tempUnit) ?: "-"}°",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(36.dp)
                    )
                }
                if (index < items.size - 1) {
                    Divider(color = Color(0xFFEAEAEA), thickness = 1.dp)
                }
            }
        }
    }
}

@Composable
private fun AirPollutionCard(data: AirPollutionDataVO) {
    WeatherCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("미세먼지", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "${airQualityText(data.pm10Quality)} (${data.pm10Density ?: "-"} ㎍/㎥)",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = (data.pm10Quality?.toIntOrNull() ?: 0) / 4f,
                    modifier = Modifier
                        .width(100.dp)
                        .height(6.dp),
                    color = airQualityColor(data.pm10Quality),
                    trackColor = Color(0xFFEAEAEA)
                )
            }
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(80.dp)
                    .background(Color(0xFFEAEAEA))
                    .align(Alignment.CenterVertically)
            )
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("초미세먼지", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "${airQualityText(data.pm2_5Quality)} (${data.pm2_5Density ?: "-"} ㎍/㎥)",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = (data.pm2_5Quality?.toIntOrNull() ?: 0) / 4f,
                    modifier = Modifier
                        .width(100.dp)
                        .height(6.dp),
                    color = airQualityColor(data.pm2_5Quality),
                    trackColor = Color(0xFFEAEAEA)
                )
            }
        }
    }
}

@Composable
private fun SunriseSunsetCard(data: SunriseSunsetVO) {
    WeatherCard {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("일출", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(formatTimeAmPmDetail(data.sunriseTime), fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_sunrise),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("일몰", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(formatTimeAmPmDetail(data.sunsetTime), fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_sunset),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )
            }
        }
    }
}

@Composable
private fun WeatherOtherInfoCard(data: WeatherOtherInfoVO) {
    WeatherCard {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_wind),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("바람", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                WindDirectionIconImage(windDirection = data.windDirection, size = 44.dp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("${data.windSpeed ?: "-"} m/s", fontSize = 16.sp)
            }
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(120.dp)
                    .background(Color(0xFFEAEAEA))
                    .align(Alignment.CenterVertically)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_humidity_percentage),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("습도", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "${data.humidity ?: "-"}%",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun WeatherForecastTextCard(data: WeatherForecastTextVO) {
    WeatherCard {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
            Text("날씨 전망", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = data.weatherForecastString ?: "-",
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
private fun ForecastDrawerContent(
    state: ForecastUiState,
    onCityClick: (WeatherNowCityListItemVO) -> Unit,
    onCurrentPositionClick: () -> Unit,
    onAddCityClick: () -> Unit,
    onManageCityClick: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("도시 목록", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onCurrentPositionClick() }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("📍 현재 위치", fontSize = 16.sp)
            state.currentPositionCity?.let {
                Spacer(modifier = Modifier.weight(1f))
                Text("${TemperatureParser.convert(it.temperature, state.tempUnit) ?: "-"}°", fontSize = 14.sp)
            }
        }

        Divider()
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(state.storedCityList) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCityClick(item) }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(item.city ?: "-", fontSize = 16.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    WeatherIconImage(weatherCondition = item.weatherCondition, size = 20.dp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${TemperatureParser.convert(item.temperature, state.tempUnit) ?: "-"}°", fontSize = 14.sp)
                }
                Divider()
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onAddCityClick, modifier = Modifier.fillMaxWidth()) {
            Text("+ 도시 추가")
        }
        TextButton(onClick = onManageCityClick, modifier = Modifier.fillMaxWidth()) {
            Text("도시 목록 관리")
        }
    }
}

// ────────────── Previews ──────────────

private val previewState = ForecastUiState(
    weatherNow = WeatherNowVO(city = "부산", temperature = "19", weatherCondition = "12"),
    weather24Hour = listOf(
        Weather24HourItemVO(time = "0300", weatherCondition = "13", temperature = "20", rainProbability = "0"),
        Weather24HourItemVO(time = "0600", weatherCondition = "1", temperature = "19", rainProbability = "0"),
        Weather24HourItemVO(time = "0900", weatherCondition = "1", temperature = "21", rainProbability = "0"),
        Weather24HourItemVO(time = "1200", weatherCondition = "2", temperature = "23", rainProbability = "30"),
    ),
    weatherWeekly = listOf(
        WeatherWeeklyItemVO(dayOfTheWeek = "오늘", rainProbability = "30", weatherConditionAM = "1", weatherConditionPM = "2", minTemperature = "19", maxTemperature = "25"),
        WeatherWeeklyItemVO(dayOfTheWeek = "토요일", rainProbability = "0", weatherConditionAM = "1", weatherConditionPM = "1", minTemperature = "16", maxTemperature = "29"),
        WeatherWeeklyItemVO(dayOfTheWeek = "일요일", rainProbability = "0", weatherConditionAM = "1", weatherConditionPM = "1", minTemperature = "18", maxTemperature = "30"),
    ),
    sunriseSunset = SunriseSunsetVO(sunriseTime = "0512", sunsetTime = "1930"),
    weatherOtherInfo = WeatherOtherInfoVO(humidity = "69", windDirection = "3", windSpeed = "1.2"),
    weatherForecastText = WeatherForecastTextVO(weatherForecastString = "○ (하늘상태) 이번 예보기간 구름많은 날이 많겠습니다.\n○ (기온) 이번 예보기간 아침 기온은 14~19℃, 낮 기온은 25~29℃로 예상됩니다."),
    airPollution = AirPollutionDataVO(pm2_5Density = "74", pm10Density = "82", pm2_5Quality = "3", pm10Quality = "3"),
    storedCityList = listOf(
        WeatherNowCityListItemVO(city = "서울", temperature = "22", weatherCondition = "1"),
        WeatherNowCityListItemVO(city = "제주", temperature = "25", weatherCondition = "8"),
    ),
    currentPositionCity = WeatherNowVO(city = "부산", temperature = "19", weatherCondition = "12")
)

@Preview(showBackground = true, name = "히어로 섹션")
@Composable
private fun HeroPreview() {
    MaterialTheme {
        ForecastHeroSection(state = previewState)
    }
}

@Preview(showBackground = true, name = "24시간 날씨")
@Composable
private fun Weather24HourPreview() {
    MaterialTheme {
        Box(modifier = Modifier.background(BgColor)) {
            Weather24HourCard(items = previewState.weather24Hour, tempUnit = WeatherTempUnit.CELSIUS)
        }
    }
}

@Preview(showBackground = true, name = "주간 날씨")
@Composable
private fun WeatherWeeklyPreview() {
    MaterialTheme {
        Box(modifier = Modifier.background(BgColor)) {
            WeatherWeeklyCard(items = previewState.weatherWeekly, tempUnit = WeatherTempUnit.CELSIUS)
        }
    }
}

@Preview(showBackground = true, name = "대기 오염")
@Composable
private fun AirPollutionPreview() {
    MaterialTheme {
        Box(modifier = Modifier.background(BgColor)) {
            AirPollutionCard(data = previewState.airPollution!!)
        }
    }
}

@Preview(showBackground = true, name = "일출/일몰")
@Composable
private fun SunriseSunsetPreview() {
    MaterialTheme {
        Box(modifier = Modifier.background(BgColor)) {
            SunriseSunsetCard(data = previewState.sunriseSunset!!)
        }
    }
}

@Preview(showBackground = true, name = "바람/습도")
@Composable
private fun OtherInfoPreview() {
    MaterialTheme {
        Box(modifier = Modifier.background(BgColor)) {
            WeatherOtherInfoCard(data = previewState.weatherOtherInfo!!)
        }
    }
}
