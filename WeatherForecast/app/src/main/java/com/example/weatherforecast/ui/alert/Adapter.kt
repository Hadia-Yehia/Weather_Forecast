package com.example.weatherforecast.ui.alert

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.weatherforecast.Model.*
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.AlertItemBinding
import com.example.weatherforecast.databinding.DayItemBinding
import com.example.weatherforecast.databinding.HourItemBinding
import java.text.SimpleDateFormat
import java.util.*


class Adapter(val onDelete:(alertModel:AlertModel)-> Unit):ListAdapter<AlertModel,Adapter.AlertViewHolder>(AlertDiffUtil()) {
    lateinit var context: Context
    lateinit var binding: AlertItemBinding

    inner class AlertViewHolder (var binding :AlertItemBinding):ViewHolder(binding.root){
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        context=parent.context
        val inflater:LayoutInflater=parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding= AlertItemBinding.inflate(inflater,parent,false)
        return AlertViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val currentObj=getItem(position)
   holder.binding.countryAlert.text=currentObj.lat+" "+currentObj.lon
        holder.binding.fromDate.text="${dateToString(currentObj.startDate?:0)}"
        holder.binding.fromTime.text="${timeToString(currentObj.startTime)}"
        holder.binding.toDate.text="${dateToString(currentObj.endDate)}"
       holder.binding.toTime.text="${timeToString(currentObj.endTime)}"
        if (currentObj.isNotification){
            holder.binding.notificationVsAlarm.setImageResource(R.drawable.notification)
        }
        else{
            holder.binding.notificationVsAlarm.setImageResource(R.drawable.alarm)
        }
        holder.binding.deleteBtn.setOnClickListener {
            onDelete(currentObj)
        }


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

class AlertDiffUtil: DiffUtil.ItemCallback<AlertModel>(){
    override fun areItemsTheSame(oldItem: AlertModel, newItem: AlertModel): Boolean {
        return (oldItem.id==newItem.id)
    }

    override fun areContentsTheSame(oldItem: AlertModel, newItem: AlertModel): Boolean {
        return oldItem==newItem
    }


}