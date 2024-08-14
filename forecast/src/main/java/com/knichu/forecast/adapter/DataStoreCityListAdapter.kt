package com.knichu.forecast.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.knichu.common_ui.databinding.LayoutCityListItemBinding
import com.knichu.domain.vo.WeatherNowCityListItemVO

class DataStoreCityListAdapter :
    ListAdapter<WeatherNowCityListItemVO, DataStoreCityListAdapter.ViewHolder>(
        DataStoreCityDiffCallback()
    ) {

    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutCityListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cityFilter = getItem(position)
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            listener?.onItemClick(cityFilter)
        }

    }

    class ViewHolder(
        private val binding: LayoutCityListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WeatherNowCityListItemVO) {
            binding.apply {
                data = item
                executePendingBindings()
            }
        }
    }

    interface Listener {
        fun onItemClick(item: WeatherNowCityListItemVO)
    }

}

private class DataStoreCityDiffCallback : DiffUtil.ItemCallback<WeatherNowCityListItemVO>() {
    override fun areItemsTheSame(
        oldItem: WeatherNowCityListItemVO,
        newItem: WeatherNowCityListItemVO
    ): Boolean {
        return oldItem.city == newItem.city
    }

    override fun areContentsTheSame(
        oldItem: WeatherNowCityListItemVO,
        newItem: WeatherNowCityListItemVO
    ): Boolean {
        return oldItem == newItem
    }
}