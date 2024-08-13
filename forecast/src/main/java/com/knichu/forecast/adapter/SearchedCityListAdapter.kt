package com.knichu.forecast.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.knichu.domain.vo.CityLocationItemVO
import com.knichu.forecast.databinding.LayoutSearchedCityListItemBinding

class SearchedCityListAdapter :
    ListAdapter<CityLocationItemVO, SearchedCityListAdapter.ViewHolder>(
        SearchedCityDiffCallback()
    ) {

    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutSearchedCityListItemBinding.inflate(
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
        private val binding: LayoutSearchedCityListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CityLocationItemVO) {
            binding.apply {
                data = item
                executePendingBindings()
            }
        }
    }

    interface Listener {
        fun onItemClick(item: CityLocationItemVO)
    }

}

private class SearchedCityDiffCallback : DiffUtil.ItemCallback<CityLocationItemVO>() {
    override fun areItemsTheSame(
        oldItem: CityLocationItemVO,
        newItem: CityLocationItemVO
    ): Boolean {
        return oldItem.cityName == newItem.cityName
    }

    override fun areContentsTheSame(
        oldItem: CityLocationItemVO,
        newItem: CityLocationItemVO
    ): Boolean {
        return oldItem == newItem
    }
}