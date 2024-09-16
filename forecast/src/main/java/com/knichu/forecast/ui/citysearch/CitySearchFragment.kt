package com.knichu.forecast.ui.citysearch

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.knichu.common_ui.base.BaseViewModelFragment
import com.knichu.domain.vo.CityLocationItemVO
import com.knichu.forecast.BR
import com.knichu.forecast.R
import com.knichu.forecast.adapter.SearchedCityListAdapter
import com.knichu.forecast.databinding.FragmentCitySearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CitySearchFragment: BaseViewModelFragment<FragmentCitySearchBinding, CitySearchViewModel>(
    R.layout.fragment_city_search,
    BR.viewModel
) {

    override val viewModel: CitySearchViewModel by viewModels()

    private val searchedCityListAdapter = SearchedCityListAdapter().apply {
        listener = object : SearchedCityListAdapter.Listener {
            override fun onItemClick(item: CityLocationItemVO) {
                AlertDialog.Builder(requireContext())
                    .setTitle("${item.cityName}")
                    .setMessage("이 도시를 추가하시겠습니까?")
                    .setPositiveButton("확인") { _, _ ->
                        item.cityName?.let {
                            viewModel.updateCityList(it)
                            activity?.finish()  // 액티비티 종료
                        }
                    }
                    .setNegativeButton("취소", null)
                    .show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewDataBinding) {

            with(searchCityEditText) {
                addTextChangedListener(createCityTextWatcher())
            }

            with(searchedCityListRecyclerView) {
                adapter = searchedCityListAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }

            with(backButton) {
                setOnClickListener {
                    requireActivity().onBackPressed()
                }
            }

        }

        with(viewModel) {

            searchedCityListData.observe {
                searchedCityListAdapter.submitList(it)
            }

            toastMessage.observe { message ->
                message.let {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        viewDataBinding.searchedCityListRecyclerView.adapter = null
        super.onDestroyView()
    }

    private fun createCityTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.getFilteredCityList(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        }
    }
}