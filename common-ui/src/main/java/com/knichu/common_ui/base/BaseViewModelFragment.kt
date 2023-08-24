package com.knichu.common_ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable

// ViewModel 사용하는 BaseFragment
abstract class BaseViewModelFragment<VD : ViewDataBinding, VM : ViewModel>(
    @LayoutRes
    private val layoutResId: Int,
    private val viewModelVariableId: Int
) : Fragment() {

    abstract val viewModel: VM

    protected val compositeDisposable = CompositeDisposable()

    private var _viewDataBinding: VD? = null
    val viewDataBinding: VD
        get() = _viewDataBinding
            ?: throw IllegalStateException("viewDataBinding can not be null")

    protected val navController by lazy { findNavController() }

    fun <T : Any> LiveData<T>.observe(observer: (T) -> Unit) {
        this.observe(viewLifecycleOwner) {
            observer.invoke(it)
        }
    }

    protected inline fun <T : Any> Observable<T>.bind(
        crossinline onNext: (value: T) -> Unit
    ) {
        subscribe { onNext.invoke(it) }
            .let(compositeDisposable::add)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<VD>(inflater, layoutResId, container, false)
            .also { viewDataBinding ->
                viewDataBinding.lifecycleOwner = viewLifecycleOwner
                viewDataBinding.setVariable(viewModelVariableId, viewModel)
                this._viewDataBinding = viewDataBinding
            }.root
    }

    override fun onDestroyView() {
        compositeDisposable.clear()

        _viewDataBinding = null
        super.onDestroyView()
    }
}