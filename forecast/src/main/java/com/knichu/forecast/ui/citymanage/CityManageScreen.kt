package com.knichu.forecast.ui.citymanage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CityManageScreen(
    viewModel: CityManageViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is CityManageUiEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("확인") },
            text = { Text("선택한 도시들을 삭제하시겠습니까?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.handleIntent(CityManageUiIntent.DeleteSelectedCities)
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
                    IconButton(onClick = { viewModel.handleIntent(CityManageUiIntent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "뒤로"
                        )
                    }
                },
                actions = {
                    TextButton(onClick = { viewModel.handleIntent(CityManageUiIntent.ToggleSelectionMode) }) {
                        Text(if (state.isInSelectionMode) "취소" else "선택")
                    }
                }
            )
        },
        bottomBar = {
            AnimatedVisibility(visible = state.isInSelectionMode && state.selectedCitySet.isNotEmpty()) {
                Button(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) { Text("삭제 (${state.selectedCitySet.size})") }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.cityList.isEmpty()) {
                Text(
                    text = "저장된 도시가 없습니다",
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 16.sp
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.cityList) { city ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .combinedClickable(
                                    onClick = {
                                        if (state.isInSelectionMode) {
                                            viewModel.handleIntent(CityManageUiIntent.ToggleCitySelection(city))
                                        }
                                    },
                                    onLongClick = {
                                        if (!state.isInSelectionMode) {
                                            viewModel.handleIntent(CityManageUiIntent.ToggleSelectionMode)
                                        }
                                    }
                                )
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(city, fontSize = 16.sp)
                            AnimatedVisibility(visible = state.isInSelectionMode) {
                                Checkbox(
                                    checked = city in state.selectedCitySet,
                                    onCheckedChange = {
                                        viewModel.handleIntent(CityManageUiIntent.ToggleCitySelection(city))
                                    }
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
