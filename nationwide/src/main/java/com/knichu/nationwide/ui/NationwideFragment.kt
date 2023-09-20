package com.knichu.nationwide.ui

import android.os.Bundle
import com.knichu.nationwide.BR
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.knichu.common_ui.base.BaseViewModelFragment
import com.knichu.nationwide.R
import com.knichu.nationwide.databinding.FragmentNationwideBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NationwideFragment : BaseViewModelFragment<FragmentNationwideBinding, NationwideViewModel>(
    R.layout.fragment_nationwide,
    BR.viewModel
) {

    override val viewModel: NationwideViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nationwide, container, false)
    }

}