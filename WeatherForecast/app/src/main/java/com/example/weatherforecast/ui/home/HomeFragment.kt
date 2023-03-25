package com.example.weatherforecast.ui.home

import android.app.ActionBar
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherforecast.Model.Repository
import com.example.weatherforecast.Network.RemoteSource
import com.example.weatherforecast.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {
    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var homeViewModel: HomeViewModel
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var dailyAdapter: DailyAdapter

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModelFactory= HomeViewModelFactory(Repository.getInstance(RemoteSource.getINSTANCE()))
        homeViewModel =
            ViewModelProvider(this,homeViewModelFactory).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getDataFromApi()
        hourlyAdapter= HourlyAdapter()
        binding.hourlyRv.apply {
            this.adapter=hourlyAdapter
            layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        }

        dailyAdapter= DailyAdapter()
        binding.dailyRv.apply {
            this.adapter=dailyAdapter
            layoutManager=LinearLayoutManager(context)
        }
        homeViewModel.apiData.observe(viewLifecycleOwner){
            binding.homeCountryTxt.text=it.timezone
            binding.homeTempTxt.text=Math.ceil(it.current.temp).toInt().toString()+"°C"
            binding.homeDescTxt.text=it.current.weather.get(0).description
            Glide.with(requireActivity()).load("https://openweathermap.org/img/wn/${it.current.weather.get(0).icon}@2x.png").into(binding.homeIconImg)
            hourlyAdapter.submitList(it.hourly)
            hourlyAdapter.notifyDataSetChanged()
            dailyAdapter.submitList(it.daily)
            dailyAdapter.notifyDataSetChanged()
            binding.humidityTxt.text=it.current.humidity.toString()+"%"
            binding.dewPointTxt.text="The dew point is ${Math.ceil(it.current.dew_point)} right now"
            binding.windSpeedTxt.text=it.current.wind_speed.toString()+" km/h"
            binding.pressureTxt.text=it.current.pressure.toString()+" hPa"
            binding.cloudsTxt.text=it.current.clouds.toString()

        }


    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}