package com.knichu.common.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {
    protected val compositeDisposable = CompositeDisposable()

    fun <T : Any> Observable<T>.bind(to: MutableLiveData<T>) {
        subscribe(to::setValue) {
            it.printStackTrace()
        }.let(compositeDisposable::add)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}