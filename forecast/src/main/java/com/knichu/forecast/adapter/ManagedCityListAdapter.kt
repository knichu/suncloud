package com.knichu.forecast.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.knichu.forecast.databinding.LayoutManagedCityListItemBinding

class ManagedCityListAdapter :
    ListAdapter<String, ManagedCityListAdapter.ViewHolder>(
        ManagedCityDiffCallback()
    ) {

    var listener: Listener? = null
    var clickItem: String? = null
    private var isInSelectionMode = false
    private val selectedItemsPosition = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutManagedCityListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cityFilter = getItem(position)
        holder.bind(getItem(position))

        holder.binding.deleteCheckBox.isChecked = selectedItemsPosition.contains(position)

        holder.itemView.setOnClickListener {
            if (isInSelectionMode) {
                holder.binding.deleteCheckBox.isChecked = !holder.binding.deleteCheckBox.isChecked
            } else {
                // Handle normal click
                clickItem = cityFilter
            }
            listener?.onItemClick(cityFilter)
        }
        holder.itemView.setOnLongClickListener {
            listener?.onItemLongClick(cityFilter)
            true
        }

        holder.binding.deleteCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedItemsPosition.add(position)
            } else {
                selectedItemsPosition.remove(position)
            }
            listener?.notifyItemSelected(cityFilter)
        }

        animateCheckbox(holder, isInSelectionMode)
    }

    class ViewHolder(
        val binding: LayoutManagedCityListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.apply {
                data = item
                executePendingBindings()
            }
        }
    }

    interface Listener {
        fun onItemClick(item: String)
        fun onItemLongClick(item: String)
        fun notifyItemSelected(item: String)
    }

    fun setSelectionMode(isVisible: Boolean) {
        isInSelectionMode = isVisible
        if (!isInSelectionMode) {
            // 선택된 아이템의 위치 목록을 순회
            selectedItemsPosition.forEach { position ->
                notifyItemChanged(position) // 해당 위치의 ViewHolder를 다시 그리게 하여 체크 해제 상태로 만듬
            }
            // 선택된 아이템의 포지션 목록을 초기화
            selectedItemsPosition.clear()
        }
        notifyDataSetChanged()
    }

    fun getSelectedItems(): MutableSet<String> {
        val selectedItems = mutableSetOf<String>()
        for (position in selectedItemsPosition) {
            currentList.getOrNull(position)?.let {
                selectedItems.add(it)
            }
        }
        return selectedItems
    }

    private fun animateCheckbox(holder: ViewHolder, isVisible: Boolean) {
        Log.d("어탭터", "체킹중")
        val checkBox = holder.binding.deleteCheckBox
        val cityText = holder.binding.cityText

        if (isVisible) {
            checkBox.visibility = View.VISIBLE
            checkBox.animate()
                .alpha(1.0f)
                .translationX(0f)
                .setDuration(300)
                .start()

            cityText.animate()
                .translationX(50f) // CheckBox가 -50dp에서 0dp로 이동하므로 TextView는 50dp 이동
                .setDuration(300)
                .start()
        } else {
            checkBox.animate()
                .alpha(0.0f)
                .translationX(-50f)
                .setDuration(300)
                .withEndAction { checkBox.visibility = View.GONE }
                .start()

            cityText.animate()
                .translationX(0f) // 원래 위치로 되돌림
                .setDuration(300)
                .start()
        }
    }

}

private class ManagedCityDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(
        oldItem: String,
        newItem: String
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: String,
        newItem: String
    ): Boolean {
        return oldItem == newItem
    }
}