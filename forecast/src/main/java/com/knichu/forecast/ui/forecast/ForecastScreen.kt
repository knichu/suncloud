package com.knichu.forecast.ui.forecast

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme
import com.knichu.common_ui.enums.WeatherIcon
import com.knichu.domain.vo.AirPollutionDataVO
import com.knichu.domain.vo.SunriseSunsetVO
import com.knichu.domain.vo.Weather24HourItemVO
import com.knichu.domain.vo.WeatherForecastTextVO
import com.knichu.domain.vo.WeatherNowCityListItemVO
import com.knichu.domain.vo.WeatherNowVO
import com.knichu.domain.vo.WeatherOtherInfoVO
import com.knichu.domain.vo.WeatherWeeklyItemVO
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(
    viewModel: ForecastViewModel
) {
    val state by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(
        initialValue = if (state.isDrawerOpen) DrawerValue.Open else DrawerValue.Closed
    )
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    var showExitDialog by remember { mutableStateOf(false) }
    var showCitySelectDialog by remember { mutableStateOf<WeatherNowCityListItemVO?>(null) }
    var showCurrentPositionDialog by remember { mutableStateOf(false) }

    // 드로어 상태와 ViewModel 상태 동기화
    LaunchedEffect(state.isDrawerOpen) {
        if (state.isDrawerOpen) drawerState.open() else drawerState.close()
    }

    // 스크롤 최상단 트리거
    LaunchedEffect(state.scrollToTopTrigger) {
        if (state.scrollToTopTrigger > 0L) {
            listState.animateScrollToItem(0)
        }
    }

    // 뒤로가기 처리
    BackHandler {
        if (drawerState.isOpen) {
            scope.launch { drawerState.close() }
            viewModel.handleIntent(ForecastUiIntent.CloseDrawer)
        } else {
            showExitDialog = true
        }
    }

    // 앱 종료 다이얼로그
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

    // 도시 선택 다이얼로그
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

    // 현재 위치 선택 다이얼로그
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
                    onCityClick = { item -> showCitySelectDialog = item },
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
                        Text(
                            text = state.selectedCity ?: state.weatherNow?.city ?: "날씨",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                            viewModel.handleIntent(ForecastUiIntent.OpenDrawer)
                        }) {
                            Icon(
                                imageVector = Icons.Filled.LocationCity,
                                contentDescription = "도시 목록"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { viewModel.handleIntent(ForecastUiIntent.Refresh) }) {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = "새로고침"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    ForecastContent(state = state, listState = listState)
                }
            }
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

        // 현재 위치 항목
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
                Text("${it.temperature}°", fontSize = 14.sp)
            }
        }

        Divider()
        Spacer(modifier = Modifier.height(8.dp))

        // 저장된 도시 목록
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
                    Text("${item.temperature}° ${WeatherIcon.getLabel(item.weatherCondition)}", fontSize = 14.sp)
                }
                Divider()
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 도시 추가 버튼
        TextButton(
            onClick = onAddCityClick,
            modifier = Modifier.fillMaxWidth()
        ) { Text("+ 도시 추가") }

        // 도시 관리 버튼
        TextButton(
            onClick = onManageCityClick,
            modifier = Modifier.fillMaxWidth()
        ) { Text("도시 목록 관리") }
    }
}

@Composable
private fun ForecastContent(
    state: ForecastUiState,
    listState: androidx.compose.foundation.lazy.LazyListState
) {
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 현재 날씨
        state.weatherNow?.let { now ->
            item {
                WeatherNowSection(
                    temperature = now.temperature,
                    city = now.city,
                    condition = WeatherIcon.getLabel(now.weatherCondition)
                )
            }
        }

        // 24시간 날씨
        if (state.weather24Hour.isNotEmpty()) {
            item { Weather24HourSection(items = state.weather24Hour) }
        }

        // 주간 날씨
        if (state.weatherWeekly.isNotEmpty()) {
            item { WeatherWeeklySection(items = state.weatherWeekly) }
        }

        // 일출/일몰
        state.sunriseSunset?.let { ss ->
            item {
                InfoCard(title = "일출/일몰") {
                    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🌅 일출", fontWeight = FontWeight.Bold)
                            Text(ss.sunriseTime ?: "-")
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🌇 일몰", fontWeight = FontWeight.Bold)
                            Text(ss.sunsetTime ?: "-")
                        }
                    }
                }
            }
        }

        // 기타 정보
        state.weatherOtherInfo?.let { info ->
            item {
                InfoCard(title = "기타 정보") {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("습도: ${info.humidity ?: "-"}%")
                        Text("풍향: ${info.windDirection ?: "-"}")
                        Text("풍속: ${info.windSpeed ?: "-"} m/s")
                    }
                }
            }
        }

        // 날씨 예보 텍스트
        state.weatherForecastText?.let { ft ->
            item {
                InfoCard(title = "날씨 예보") {
                    Text(ft.weatherForecastString ?: "-", lineHeight = 22.sp)
                }
            }
        }

        // 대기 오염
        state.airPollution?.let { ap ->
            item {
                InfoCard(title = "대기 오염") {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("PM2.5: ${ap.pm2_5Density ?: "-"} (${ap.pm2_5Quality ?: "-"})")
                        Text("PM10:  ${ap.pm10Density ?: "-"} (${ap.pm10Quality ?: "-"})")
                    }
                }
            }
        }
    }
}

@Composable
private fun WeatherNowSection(temperature: String?, city: String?, condition: String?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(city ?: "-", fontSize = 20.sp)
        Text("${temperature ?: "-"}°", fontSize = 64.sp, fontWeight = FontWeight.Thin)
        Text(condition ?: "-", fontSize = 18.sp)
    }
}

@Composable
private fun Weather24HourSection(items: List<Weather24HourItemVO>) {
    InfoCard(title = "시간별 날씨") {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(items) { item ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(item.time ?: "-", fontSize = 12.sp)
                    Text(WeatherIcon.getLabel(item.weatherCondition), fontSize = 11.sp)
                    Text("${item.temperature ?: "-"}°", fontWeight = FontWeight.Bold)
                    Text("${item.rainProbability ?: "0"}%", fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
private fun WeatherWeeklySection(items: List<WeatherWeeklyItemVO>) {
    InfoCard(title = "주간 날씨") {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(item.dayOfTheWeek ?: "-", modifier = Modifier.width(36.dp))
                    Text("${item.rainProbability ?: "0"}%", fontSize = 12.sp)
                    Text("${WeatherIcon.getLabel(item.weatherConditionAM)} / ${WeatherIcon.getLabel(item.weatherConditionPM)}", fontSize = 12.sp)
                    Text("${item.minTemperature ?: "-"}° / ${item.maxTemperature ?: "-"}°")
                }
            }
        }
    }
}

@Composable
private fun InfoCard(title: String, content: @Composable () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

// ────────────── Previews ──────────────

private val previewState = ForecastUiState(
    weatherNow = WeatherNowVO(city = "서울", temperature = "23", weatherCondition = "맑음"),
    weather24Hour = listOf(
        Weather24HourItemVO(time = "1400", weatherCondition = "맑음", temperature = "23", rainProbability = "10"),
        Weather24HourItemVO(time = "1700", weatherCondition = "구름많음", temperature = "21", rainProbability = "20"),
        Weather24HourItemVO(time = "2000", weatherCondition = "흐림", temperature = "18", rainProbability = "40"),
        Weather24HourItemVO(time = "2300", weatherCondition = "비", temperature = "16", rainProbability = "70"),
    ),
    weatherWeekly = listOf(
        WeatherWeeklyItemVO(dayOfTheWeek = "월", rainProbability = "10", weatherConditionAM = "맑음", weatherConditionPM = "맑음", minTemperature = "15", maxTemperature = "25"),
        WeatherWeeklyItemVO(dayOfTheWeek = "화", rainProbability = "30", weatherConditionAM = "구름", weatherConditionPM = "비", minTemperature = "14", maxTemperature = "22"),
        WeatherWeeklyItemVO(dayOfTheWeek = "수", rainProbability = "60", weatherConditionAM = "비", weatherConditionPM = "흐림", minTemperature = "13", maxTemperature = "19"),
    ),
    sunriseSunset = SunriseSunsetVO(sunriseTime = "0532", sunsetTime = "1948"),
    weatherOtherInfo = WeatherOtherInfoVO(humidity = "55", windDirection = "북서", windSpeed = "3.2"),
    weatherForecastText = WeatherForecastTextVO(weatherForecastString = "오늘은 전국적으로 맑은 날씨가 예상됩니다. 낮 최고기온은 25도 내외입니다."),
    airPollution = AirPollutionDataVO(pm2_5Density = "12", pm10Density = "24", pm2_5Quality = "좋음", pm10Quality = "보통"),
    storedCityList = listOf(
        WeatherNowCityListItemVO(city = "부산", temperature = "25", weatherCondition = "맑음"),
        WeatherNowCityListItemVO(city = "제주", temperature = "27", weatherCondition = "구름많음"),
    ),
    currentPositionCity = WeatherNowVO(city = "서울", temperature = "23", weatherCondition = "맑음")
)

@Preview(showBackground = true, name = "날씨 메인 콘텐츠")
@Composable
private fun ForecastContentPreview() {
    MaterialTheme {
        ForecastContent(
            state = previewState,
            listState = rememberLazyListState()
        )
    }
}

@Preview(showBackground = true, name = "날씨 메인 콘텐츠 - 로딩")
@Composable
private fun ForecastContentLoadingPreview() {
    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Preview(showBackground = true, widthDp = 320, name = "드로어 - 도시 목록")
@Composable
private fun ForecastDrawerPreview() {
    MaterialTheme {
        ForecastDrawerContent(
            state = previewState,
            onCityClick = {},
            onCurrentPositionClick = {},
            onAddCityClick = {},
            onManageCityClick = {}
        )
    }
}
