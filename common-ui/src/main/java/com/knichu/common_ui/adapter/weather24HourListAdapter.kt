package com.knichu.common_ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.knichu.common_ui.databinding.LayoutWeather24hourItemBinding
import com.knichu.domain.vo.Weather24HourItemVO

class Weather24HourListAdapter :
    ListAdapter<Weather24HourItemVO, Weather24HourListAdapter.ViewHolder>(
        Weather24HourDiffCallback()
    ) {

    // 추후 이 listener로 클릭 이벤트 처리
    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutWeather24hourItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weather24HourFilter = getItem(position)
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            listener?.onItemClick(weather24HourFilter)
        }
    }

    class ViewHolder(
        private val binding: LayoutWeather24hourItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Weather24HourItemVO) {
            binding.apply {
                data = item
                executePendingBindings()
            }
        }
    }

    interface Listener {
        fun onItemClick(item: Weather24HourItemVO)
    }
}

private class Weather24HourDiffCallback : DiffUtil.ItemCallback<Weather24HourItemVO>() {
    override fun areItemsTheSame(
        oldItem: Weather24HourItemVO,
        newItem: Weather24HourItemVO
    ): Boolean {
        return oldItem.time == newItem.time
    }

    override fun areContentsTheSame(
        oldItem: Weather24HourItemVO,
        newItem: Weather24HourItemVO
    ): Boolean {
        return oldItem == newItem
    }
}