package com.knichu.common_ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.knichu.common_ui.R
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable

// ViewModel 사용하는 BaseActivity
abstract class BaseViewModelActivity<VD : ViewDataBinding, VM : ViewModel>(
    @LayoutRes
    private val layoutResId: Int,
    private val viewModelVariableId: Int
) : AppCompatActivity() {

    abstract val viewModel: VM

    lateinit var viewDataBinding: VD

    protected val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.none)

        viewDataBinding =
            DataBindingUtil.setContentView<VD>(this, layoutResId).also { viewDataBinding ->
                viewDataBinding.lifecycleOwner = this
                viewDataBinding.setVariable(viewModelVariableId, viewModel)
                this.viewDataBinding = viewDataBinding
            }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.none, R.anim.slide_out_to_right)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            overridePendingTransition(R.anim.none, R.anim.slide_out_to_right)
        }
    }

    fun <T : Any> LiveData<T>.observe(observer: (T) -> Unit) {
        this.observe(this@BaseViewModelActivity) {
            observer.invoke(it)
        }
    }

    protected inline fun <T : Any> Observable<T>.bind(
        crossinline onNext: (value: T) -> Unit
    ) {
        subscribe { onNext.invoke(it) }
            .let(compositeDisposable::add)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}