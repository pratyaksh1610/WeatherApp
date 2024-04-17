package com.pratyakshkhurana.weatherapp.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pratyakshkhurana.weatherapp.DataClass.EveryThreeHourRecyclerView
import com.pratyakshkhurana.weatherapp.databinding.RecyclerViewEveryThreeHourWeatherItemBinding

class EveryThreeHourAdapter(
    private val context: Context,
    val data: MutableList<EveryThreeHourRecyclerView>,
) :
    RecyclerView.Adapter<EveryThreeHourAdapter.EveryThreeHourViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): EveryThreeHourViewHolder {
        val viewBinding =
            RecyclerViewEveryThreeHourWeatherItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return EveryThreeHourViewHolder(viewBinding)
    }

    override fun onBindViewHolder(
        holder: EveryThreeHourViewHolder,
        position: Int,
    ) {
        holder.timeAmPm.text = data[position].time
        holder.temp.text = data[position].temp + "°C"
        Log.e("qq", "onBindViewHolder: ${data[position].codeImg}")
        Glide
            .with(context)
            .load("https://openweathermap.org/img/wn/${data[position].codeImg}@2x.png")
            .centerCrop()
            .into(holder.icon)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class EveryThreeHourViewHolder(binding: RecyclerViewEveryThreeHourWeatherItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val timeAmPm = binding.timeAmPm
        val temp = binding.temp
        val icon = binding.weatherIcon
    }
}
