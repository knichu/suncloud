package com.knichu.common_ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.knichu.common_ui.R
import io.reactivex.rxjava3.disposables.CompositeDisposable

// ViewModel 사용안하는 BaseActivity
abstract class BaseActivity<VD : ViewDataBinding>(
    @LayoutRes
    private val layoutResId: Int
) : AppCompatActivity() {

    lateinit var viewDataBinding: VD
    protected val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.none)

        viewDataBinding =
            DataBindingUtil.setContentView<VD>(this, layoutResId).also { viewDataBinding ->
                viewDataBinding.lifecycleOwner = this
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

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}