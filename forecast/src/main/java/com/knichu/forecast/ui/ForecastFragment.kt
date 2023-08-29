package com.knichu.forecast.ui

import android.os.Bundle
import com.knichu.forecast.BR
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.knichu.common_ui.base.BaseViewModelFragment
import com.knichu.forecast.R
import com.knichu.forecast.databinding.FragmentForecastBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForecastFragment : BaseViewModelFragment<FragmentForecastBinding, ForecastViewModel>(
    R.layout.fragment_forecast,
    BR.viewModel
) {

    override val viewModel: ForecastViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forecast, container, false)
    }

}