package com.knichu.forecast.ui.citysearch

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.knichu.domain.vo.CityLocationItemVO

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySearchScreen(
    viewModel: CitySearchViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showAddDialog by remember { mutableStateOf<CityLocationItemVO?>(null) }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is CitySearchUiEffect.NavigateBack      -> onNavigateBack()
                is CitySearchUiEffect.ShowToast         ->
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 도시 추가 확인 다이얼로그
    showAddDialog?.let { item ->
        AlertDialog(
            onDismissRequest = { showAddDialog = null },
            title = { Text("${item.cityName}") },
            text = { Text("이 도시를 추가하시겠습니까?") },
            confirmButton = {
                TextButton(onClick = {
                    item.cityName?.let { viewModel.handleIntent(CitySearchUiIntent.AddCity(it)) }
                    showAddDialog = null
                }) { Text("확인") }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = null }) { Text("취소") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("도시 검색", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.handleIntent(CitySearchUiIntent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "뒤로"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.handleIntent(CitySearchUiIntent.Search(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("도시 이름을 입력하세요") },
                singleLine = true
            )

            Box(modifier = Modifier.fillMaxSize()) {
                if (state.searchedCityList.isEmpty() && state.searchQuery.isNotEmpty()) {
                    Text(
                        text = "검색 결과가 없습니다",
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 16.sp
                    )
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.searchedCityList) { item ->
                            Text(
                                text = item.cityName ?: "-",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showAddDialog = item }
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                fontSize = 16.sp
                            )
                            Divider()
                        }
                    }
                }
            }
        }
    }
}
