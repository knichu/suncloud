package com.knichu.setting.ui

import android.os.Bundle
import com.knichu.setting.BR
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.knichu.common_ui.base.BaseViewModelFragment
import com.knichu.setting.R
import com.knichu.setting.databinding.FragmentSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : BaseViewModelFragment<FragmentSettingBinding, SettingViewModel>(
    R.layout.fragment_setting,
    BR.viewModel
) {

    override val viewModel: SettingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

}