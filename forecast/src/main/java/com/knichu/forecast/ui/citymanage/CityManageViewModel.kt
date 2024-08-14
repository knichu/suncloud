package com.knichu.forecast.ui.citymanage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.knichu.common.base.BaseViewModel
import com.knichu.domain.useCase.DataStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CityManageViewModel @Inject constructor(
    private val dataStoreUseCase: DataStoreUseCase
) : BaseViewModel() {
    private val _isInSelectionMode: MutableLiveData<Boolean> = MutableLiveData(false)
    val isInSelectionMode: LiveData<Boolean> = _isInSelectionMode

    private val _selectedCitySetData: MutableLiveData<MutableSet<String>> = MutableLiveData()
    val selectedCitySetData: LiveData<MutableSet<String>> = _selectedCitySetData

    private val _managedCityListData: MutableLiveData<List<String>> = MutableLiveData()
    val managedCityListData: LiveData<List<String>> = _managedCityListData

    init {
        getCityList()
    }

    private fun getCityList() {
        dataStoreUseCase.getCityList()
            .applySchedulers()
            .toObservable()
            .bind(_managedCityListData)
    }

    fun deleteCity(selectedCitySet: MutableSet<String>) {
        dataStoreUseCase.deleteCity(selectedCitySet)
            .applySchedulers()
            .subscribe(
                {}, { error ->
                    Log.e("체크", "CitySearchViewModel, Error deleting city list: ${error.message}")
                }
            ).let(compositeDisposable::add)
    }

    fun changeSelectionMode() {
        _isInSelectionMode.value = !(_isInSelectionMode.value?: true)
    }

    fun setSelectedCityListEmpty() {
        _selectedCitySetData.value = mutableSetOf()
    }

    fun setSelectedCityList(stringSet: MutableSet<String>) {
        _selectedCitySetData.value = stringSet
    }

}