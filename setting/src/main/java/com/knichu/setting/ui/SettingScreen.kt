package com.knichu.setting.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.knichu.domain.constants.WeatherTempUnit

@Composable
fun SettingScreen(viewModel: SettingViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    SettingContent(
        tempUnit = uiState.tempUnit,
        onTempUnitChanged = { viewModel.handleIntent(SettingUiIntent.OnTempUnitChanged(it)) }
    )
}

@Composable
private fun SettingContent(
    tempUnit: WeatherTempUnit,
    onTempUnitChanged: (WeatherTempUnit) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Text(
            text = "설정",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp)
        )

        SectionHeader("온도 단위")

        TempUnitRow(
            label = "섭씨 (°C)",
            selected = tempUnit == WeatherTempUnit.CELSIUS,
            onClick = { onTempUnitChanged(WeatherTempUnit.CELSIUS) }
        )
        Divider(modifier = Modifier.padding(start = 20.dp))
        TempUnitRow(
            label = "화씨 (°F)",
            selected = tempUnit == WeatherTempUnit.FAHRENHEIT,
            onClick = { onTempUnitChanged(WeatherTempUnit.FAHRENHEIT) }
        )
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 13.sp,
        color = Color(0xFF888888),
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
    )
}

@Composable
private fun TempUnitRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Text(
            text = label,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingContentPreview() {
    MaterialTheme {
        SettingContent(
            tempUnit = WeatherTempUnit.CELSIUS,
            onTempUnitChanged = {}
        )
    }
}
