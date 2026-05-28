package com.knichu.forecast.ui.citymanage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ── Stateful (ViewModel 연결) ──
@Composable
fun CityManageScreen(
    viewModel: CityManageViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is CityManageUiEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    CityManageScreen(
        cityList = state.cityList,
        isInSelectionMode = state.isInSelectionMode,
        selectedCitySet = state.selectedCitySet,
        onToggleSelectionMode = { viewModel.handleIntent(CityManageUiIntent.ToggleSelectionMode) },
        onToggleCitySelection = { viewModel.handleIntent(CityManageUiIntent.ToggleCitySelection(it)) },
        onDeleteSelected = { viewModel.handleIntent(CityManageUiIntent.DeleteSelectedCities) },
        onNavigateBack = { viewModel.handleIntent(CityManageUiIntent.NavigateBack) }
    )
}

// ── Stateless (Preview 가능) ──
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CityManageScreen(
    cityList: List<String>,
    isInSelectionMode: Boolean,
    selectedCitySet: Set<String>,
    onToggleSelectionMode: () -> Unit,
    onToggleCitySelection: (String) -> Unit,
    onDeleteSelected: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("확인") },
            text = { Text("선택한 도시들을 삭제하시겠습니까?") },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteSelected()
                    showDeleteDialog = false
                }) { Text("확인") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("취소") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("도시 관리", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "뒤로")
                    }
                },
                actions = {
                    TextButton(onClick = onToggleSelectionMode) {
                        Text(if (isInSelectionMode) "취소" else "선택")
                    }
                }
            )
        },
        bottomBar = {
            AnimatedVisibility(visible = isInSelectionMode && selectedCitySet.isNotEmpty()) {
                Button(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) { Text("삭제 (${selectedCitySet.size})") }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (cityList.isEmpty()) {
                Text(
                    text = "저장된 도시가 없습니다",
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 16.sp
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(cityList) { city ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .combinedClickable(
                                    onClick = {
                                        if (isInSelectionMode) onToggleCitySelection(city)
                                    },
                                    onLongClick = {
                                        if (!isInSelectionMode) onToggleSelectionMode()
                                    }
                                )
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(city, fontSize = 16.sp)
                            AnimatedVisibility(visible = isInSelectionMode) {
                                Checkbox(
                                    checked = city in selectedCitySet,
                                    onCheckedChange = { onToggleCitySelection(city) }
                                )
                            }
                        }
                        Divider()
                    }
                }
            }
        }
    }
}

// ────────────── Previews ──────────────

@Preview(showBackground = true, name = "도시 관리 - 일반")
@Composable
private fun CityManageScreenPreview() {
    MaterialTheme {
        Surface {
            CityManageScreen(
                cityList = listOf("서울", "부산", "제주", "대구", "인천"),
                isInSelectionMode = false,
                selectedCitySet = emptySet(),
                onToggleSelectionMode = {},
                onToggleCitySelection = {},
                onDeleteSelected = {},
                onNavigateBack = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "도시 관리 - 선택 모드")
@Composable
private fun CityManageScreenSelectionPreview() {
    MaterialTheme {
        Surface {
            CityManageScreen(
                cityList = listOf("서울", "부산", "제주", "대구", "인천"),
                isInSelectionMode = true,
                selectedCitySet = setOf("부산", "제주"),
                onToggleSelectionMode = {},
                onToggleCitySelection = {},
                onDeleteSelected = {},
                onNavigateBack = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "도시 관리 - 비어있음")
@Composable
private fun CityManageScreenEmptyPreview() {
    MaterialTheme {
        Surface {
            CityManageScreen(
                cityList = emptyList(),
                isInSelectionMode = false,
                selectedCitySet = emptySet(),
                onToggleSelectionMode = {},
                onToggleCitySelection = {},
                onDeleteSelected = {},
                onNavigateBack = {}
            )
        }
    }
}
