package com.knichu.forecast.ui.citysearch

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.knichu.common.base.BaseViewModel
import com.knichu.domain.useCase.DataStoreUseCase
import com.knichu.domain.useCase.SearchCityUseCase
import com.knichu.domain.vo.CityLocationItemVO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CitySearchViewModel @Inject constructor(
    private val searchCityUseCase: SearchCityUseCase,
    private val dataStoreUseCase: DataStoreUseCase
) : BaseViewModel() {
    private val _searchedCityListData: MutableLiveData<List<CityLocationItemVO>> = MutableLiveData()
    val searchedCityListData: LiveData<List<CityLocationItemVO>> = _searchedCityListData

    private val _toastMessage: MutableLiveData<String> = MutableLiveData()
    val toastMessage: LiveData<String> = _toastMessage

    fun getFilteredCityList(editedText: String) {
        searchCityUseCase.getFilteredCityList(editedText)
            .applySchedulers()
            .toObservable()
            .map{ it.item?: emptyList() }
            .bind(_searchedCityListData)
    }

    fun updateCityList(selectedCity: String) {
        dataStoreUseCase.storeCity(selectedCity)
            .applySchedulers()
            .subscribe(
                {}, { _toastMessage.postValue("이미 도시가 추가되어 있습니다") }
            ).let(compositeDisposable::add)
    }
}