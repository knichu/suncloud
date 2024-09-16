package com.knichu.setting.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.knichu.common.base.BaseViewModel
import com.knichu.domain.useCase.DataStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val dataStoreUseCase: DataStoreUseCase
) : BaseViewModel() {
    private val _data: MutableLiveData<String> = MutableLiveData()
    val data: LiveData<String> = _data
}