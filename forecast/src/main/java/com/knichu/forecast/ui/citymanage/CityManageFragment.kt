package com.knichu.forecast.ui.citymanage

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.knichu.common_ui.base.BaseViewModelFragment
import com.knichu.domain.vo.CityLocationItemVO
import com.knichu.forecast.BR
import com.knichu.forecast.R
import com.knichu.forecast.adapter.ManagedCityListAdapter
import com.knichu.forecast.databinding.FragmentCityManageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CityManageFragment : BaseViewModelFragment<FragmentCityManageBinding, CityManageViewModel>(
    R.layout.fragment_city_manage,
    BR.viewModel
) {

    override val viewModel: CityManageViewModel by viewModels()

    private val managedCityListAdapter = ManagedCityListAdapter().apply {
        listener = object : ManagedCityListAdapter.Listener {
            override fun onItemClick(item: String) {
                if (viewModel.isInSelectionMode.value == false) {
//                    AlertDialog.Builder(requireContext())
//                        .setTitle("$clickItem")
//                        .setMessage("이 도시를 선택하겠습니까?")
//                        .setPositiveButton("확인") { _, _ ->
////                            item.cityName?.let {
////                                viewModel.updateCityList(it)
////                                activity?.finish()  // 액티비티 종료
////                            }
//                        }
//                        .setNegativeButton("취소", null)
//                        .show()
                }
            }

            override fun onItemLongClick(item: String) {
                Log.d("테스트", "뷰모델 롱클릭")
                viewModel.changeSelectionMode()
                if (viewModel.isInSelectionMode.value == false) {
                    viewModel.setSelectedCityListEmpty()
                }
            }

            override fun notifyItemSelected(item: String) {
                viewModel.setSelectedCityList(getSelectedItems())
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewDataBinding) {

            with(managedCityListRecyclerView) {
                adapter = managedCityListAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }

            with(backButton) {
                setOnClickListener {
                    requireActivity().onBackPressed()
                }
            }

            with(activateDeleteCheckBoxButton) {
                setOnClickListener {
                    viewModel?.changeSelectionMode()
                }
            }

            with(deleteButton) {
                setOnClickListener {
                    AlertDialog.Builder(requireContext())
                        .setTitle("${viewModel?.selectedCitySetData?.value}")
                        .setMessage("선택한 도시들을 삭제하겠습니까?")
                        .setPositiveButton("확인") { _, _ ->
                            viewModel?.selectedCitySetData?.value?.let {
                                viewModel?.deleteCity(it)
                            }
                            viewModel?.setSelectedCityListEmpty()
                            viewModel?.changeSelectionMode()
                        }
                        .setNegativeButton("취소", null)
                        .show()
                }
            }
        }

        with(viewModel) {
            managedCityListData.observe {
                managedCityListAdapter.submitList(it)
            }

            isInSelectionMode.observe {
                managedCityListAdapter.setSelectionMode(it)
            }

            selectedCitySetData.observe {
                activateDeleteButton(it.isNotEmpty())
            }
        }
    }

    override fun onDestroyView() {
        viewDataBinding.managedCityListRecyclerView.adapter = null
        super.onDestroyView()
    }

    private fun activateDeleteButton(isNotEmpty: Boolean) {
        viewDataBinding.deleteButton.apply {
            if (isNotEmpty) {
                visibility = View.VISIBLE
                animate()
                    .translationY(0f)
                    .alpha(1.0f)
                    .setDuration(300)
                    .start()
            } else {
                animate()
                    .translationY(height.toFloat())
                    .alpha(0.0f)
                    .setDuration(300)
                    .withEndAction { visibility = View.GONE }
                    .start()
            }
        }
    }

}