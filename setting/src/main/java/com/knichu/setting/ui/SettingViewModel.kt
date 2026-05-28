package com.knichu.setting.ui

import com.knichu.common.base.BaseMviViewModel
import com.knichu.domain.useCase.DataStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val dataStoreUseCase: DataStoreUseCase
) : BaseMviViewModel<SettingUiIntent, SettingUiState, SettingUiEffect>() {

    override fun initialState() = SettingUiState()

    override fun handleIntent(intent: SettingUiIntent) {}
}
