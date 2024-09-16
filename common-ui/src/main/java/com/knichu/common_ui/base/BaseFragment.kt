package com.knichu.common_ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable

// ViewModel 사용안하는 BaseFragment
abstract class BaseFragment<VD : ViewDataBinding>(
    @LayoutRes
    private val layoutResId: Int
) : Fragment() {

    protected val compositeDisposable = CompositeDisposable()

    private var _viewDataBinding: VD? = null
    val viewDataBinding: VD
        get() = _viewDataBinding
            ?: throw IllegalStateException("viewDataBinding can not be null")

    protected val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<VD>(inflater, layoutResId, container, false)
            .also { viewDataBinding ->
                viewDataBinding.lifecycleOwner = viewLifecycleOwner
                this._viewDataBinding = viewDataBinding
            }.root
    }

    override fun onDestroyView() {
        _viewDataBinding = null
        compositeDisposable.clear()
        super.onDestroyView()
    }

    protected inline fun <T : Any> Observable<T>.bind(
        crossinline onNext: (value: T) -> Unit
    ) {
        subscribe { onNext.invoke(it) }
            .let(compositeDisposable::add)
    }
}