package com.knichu.common_ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.knichu.common_ui.databinding.LayoutWeather24hourItemBinding
import com.knichu.common_ui.databinding.LayoutWeatherWeeklyItemBinding
import com.knichu.domain.vo.Weather24HourItemVO
import com.knichu.domain.vo.WeatherWeeklyItemVO

class WeatherWeeklyListAdapter :
    ListAdapter<WeatherWeeklyItemVO, WeatherWeeklyListAdapter.ViewHolder>(
        WeatherWeeklyDiffCallback()
    ) {

    // 추후 이 listener로 클릭 이벤트 처리
    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutWeatherWeeklyItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weatherWeeklyFilter = getItem(position)
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            listener?.onItemClick(weatherWeeklyFilter)
        }
    }

    class ViewHolder(
        private val binding: LayoutWeatherWeeklyItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WeatherWeeklyItemVO) {
            binding.apply {
                data = item
                executePendingBindings()
            }
        }
    }

    interface Listener {
        fun onItemClick(item: WeatherWeeklyItemVO)
    }
}

private class WeatherWeeklyDiffCallback : DiffUtil.ItemCallback<WeatherWeeklyItemVO>() {
    override fun areItemsTheSame(
        oldItem: WeatherWeeklyItemVO,
        newItem: WeatherWeeklyItemVO
    ): Boolean {
        return oldItem.dayOfTheWeek == newItem.dayOfTheWeek
    }

    override fun areContentsTheSame(
        oldItem: WeatherWeeklyItemVO,
        newItem: WeatherWeeklyItemVO
    ): Boolean {
        return oldItem == newItem
    }
}