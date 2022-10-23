package com.example.weather.view

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.databinding.ForecastCardBinding
import com.example.weather.model.SingleForecast

class WeekForecastAdapter(private val context: Context) :
    RecyclerView.Adapter<WeekForecastAdapter.WeekForecastViewHolder>() {

    var weekForecast = listOf<SingleForecast>()
    fun setData(data: List<SingleForecast>) {
        weekForecast = data
        notifyDataSetChanged()
    }

    var status = View.GONE
    fun setSt(st: Int) {
        status = st
        notifyDataSetChanged()
    }

    class WeekForecastViewHolder(
        private var binding: ForecastCardBinding,
        private val context: Context,
        private val status: Int
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SingleForecast) {
            binding.progressBar2.visibility = status
            binding.forecast = item
            val identifier = context.resources.getIdentifier(
                item.icon ?: "mock",
                "drawable",
                context.packageName
            )
            binding.icon.setImageResource(identifier)
            if (item.flag) {
                binding.date.setTextColor(Color.parseColor("#fafafa"))
            } else {
                binding.date.setTextColor(Color.parseColor("#000000"))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekForecastViewHolder {
        return WeekForecastViewHolder(
            ForecastCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            context,
            status
        )
    }

    override fun onBindViewHolder(holder: WeekForecastViewHolder, position: Int) {
        holder.bind(weekForecast[position])
    }

    override fun getItemCount(): Int = weekForecast.size
}
