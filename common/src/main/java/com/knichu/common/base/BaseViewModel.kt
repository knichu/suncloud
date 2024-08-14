package com.knichu.common.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

abstract class BaseViewModel : ViewModel() {
    protected val compositeDisposable = CompositeDisposable()

    fun <T : Any> Single<T>.applySchedulers() =
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    fun Completable.applySchedulers() =
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    fun <T : Any> Observable<T>.applySchedulers() =
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    fun <T : Any> Flowable<T>.applySchedulers() =
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

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