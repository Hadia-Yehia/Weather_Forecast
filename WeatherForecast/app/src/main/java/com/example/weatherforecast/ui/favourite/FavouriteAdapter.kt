package com.example.weatherforecast.ui.favourite

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.weatherforecast.Model.Current
import com.example.weatherforecast.Model.Welcome
import com.example.weatherforecast.databinding.FavItemBinding
import com.example.weatherforecast.databinding.HourItemBinding
import java.text.SimpleDateFormat
import java.util.*


class FavouriteAdapter(val delete:onFavDeleteClick,private val onClick:(Welcome)->Unit ):ListAdapter<Welcome,FavouriteAdapter.FavouriteViewHolder>(HourDiffUtil()) {
    lateinit var context: Context
    lateinit var binding: FavItemBinding

    inner class FavouriteViewHolder (var binding: FavItemBinding):ViewHolder(binding.root){
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        context=parent.context
        val inflater:LayoutInflater=parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding= FavItemBinding.inflate(inflater,parent,false)
        return FavouriteViewHolder(binding)
    }

    private fun getCurrentTime(dt: Int): String {
        var date= Date(dt*1000L)
        var sdf= SimpleDateFormat("hh:mm a")
        sdf.timeZone= TimeZone.getDefault()
        return sdf.format(date)

    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val currentObj = getItem(position)
        var time: String = getCurrentTime(currentObj.current.dt.toInt())
        holder.binding.favCountryTxt.text = currentObj.timezone
//        holder.binding.favDescTxt.text = currentObj.current.weather.get(0).description
//        holder.binding.favTimeTxt.text = time
//        holder.binding.favTempTxt.text =
//            Math.ceil(currentObj.current.temp).toInt().toString() + "°C"
//        holder.binding.favHighTxt.text =
//            Math.ceil(currentObj.daily.get(0).temp.max).toInt().toString() + "°C"
//        holder.binding.favLowTxt.text =
//            Math.ceil(currentObj.daily.get(0).temp.min).toInt().toString() + "°C"
        holder.binding.favLayout.setOnClickListener { onClick(currentObj) }
        holder.binding.favDelete.setOnClickListener { delete.deleteFav(currentObj) }
    }
}

class HourDiffUtil: DiffUtil.ItemCallback<Welcome>(){
    override fun areItemsTheSame(oldItem: Welcome, newItem: Welcome): Boolean {
        return ((oldItem.lat==newItem.lat) && (oldItem.lon==oldItem.lon))
    }

    override fun areContentsTheSame(oldItem: Welcome, newItem: Welcome): Boolean {
        return oldItem==newItem
    }

}