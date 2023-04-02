package com.example.weatherforecast.ui.favourite

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.weatherforecast.Model.Current
import com.example.weatherforecast.Model.Daily
import com.example.weatherforecast.databinding.DayItemBinding
import com.example.weatherforecast.databinding.HourItemBinding
import java.text.SimpleDateFormat
import java.util.*


class FavDailyAdapter:ListAdapter<Daily,FavDailyAdapter.FavDailyViewHolder>(FavDayDiffUtil()) {
    lateinit var context: Context
    lateinit var binding: DayItemBinding

    inner class FavDailyViewHolder (var binding :DayItemBinding):ViewHolder(binding.root){
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavDailyViewHolder {
        context=parent.context
        val inflater:LayoutInflater=parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding= DayItemBinding.inflate(inflater,parent,false)
        return FavDailyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavDailyViewHolder, position: Int) {
        val currentObj=getItem(position)
        var day:String= getCurrentDay(currentObj.dt.toInt())
        holder.binding.dayNameTxt.text=day
        holder.binding.dayHighTxt.text=Math.ceil(currentObj.temp.max).toInt().toString()+"°C"
        holder.binding.dayLowTxt.text=Math.ceil(currentObj.temp.min).toInt().toString()+"°C"
        Glide.with(context).load("https://openweathermap.org/img/wn/${currentObj.weather.get(0).icon}@2x.png").into(holder.binding.dayImg)
    }

    fun getCurrentDay( dt: Int) : String{
        var date= Date(dt*1000L)
        var sdf= SimpleDateFormat("d")
        sdf.timeZone= TimeZone.getDefault()
        var formatedData=sdf.format(date)
        var intDay=formatedData.toInt()
        var calendar= Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH,intDay)
        var format= SimpleDateFormat("EEEE")
        return format.format(calendar.time)
    }
}

class FavDayDiffUtil: DiffUtil.ItemCallback<Daily>(){
    override fun areItemsTheSame(oldItem: Daily, newItem: Daily): Boolean {
        return (oldItem.dt==newItem.dt)
    }

    override fun areContentsTheSame(oldItem: Daily, newItem: Daily): Boolean {
        return oldItem==newItem
    }


}