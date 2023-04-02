package com.example.weatherforecast.ui.favourite
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.weatherforecast.Model.Current
import com.example.weatherforecast.databinding.HourItemBinding
import java.text.SimpleDateFormat
import java.util.*


    class FavHourlyAdapter: ListAdapter<Current, FavHourlyAdapter.FavHourlyViewHolder>(FavHourDiffUtil()) {
        lateinit var context: Context
        lateinit var binding: HourItemBinding

        inner class FavHourlyViewHolder (var binding : HourItemBinding): RecyclerView.ViewHolder(binding.root){
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavHourlyViewHolder {
            context=parent.context
            val inflater: LayoutInflater =parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            binding= HourItemBinding.inflate(inflater,parent,false)
            return FavHourlyViewHolder(binding)
        }

        private fun getCurrentTime(dt: Int): String {
            var date= Date(dt*1000L)
            var sdf= SimpleDateFormat("hh:mm a")
            sdf.timeZone= TimeZone.getDefault()
            return sdf.format(date)

        }

        override fun onBindViewHolder(holder: FavHourlyViewHolder, position: Int) {
            val currentObj=getItem(position)
            var time:String= getCurrentTime(currentObj.dt.toInt())
            holder.binding.hourTxt.text=time
            holder.binding.hourTempTxt.text=Math.ceil(currentObj.temp).toInt().toString()+"°C"
            Glide.with(context).load("https://openweathermap.org/img/wn/${currentObj.weather.get(0).icon}@2x.png").into(holder.binding.hourImg)
        }
    }

    class FavHourDiffUtil: DiffUtil.ItemCallback<Current>(){
        override fun areItemsTheSame(oldItem: Current, newItem: Current): Boolean {
            return (oldItem.dt==newItem.dt)
        }

        override fun areContentsTheSame(oldItem: Current, newItem: Current): Boolean {
            return oldItem==newItem
        }


}