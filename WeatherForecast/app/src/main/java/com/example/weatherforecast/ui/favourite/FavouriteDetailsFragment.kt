package com.example.weatherforecast.ui.favourite

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherforecast.DataBase.LocalSource
import com.example.weatherforecast.Model.Repository
import com.example.weatherforecast.Model.Welcome
import com.example.weatherforecast.MyViewModel
import com.example.weatherforecast.MyViewModelFactory
import com.example.weatherforecast.Network.APIState
import com.example.weatherforecast.Network.RemoteSource
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentFavouriteDetailsBinding
import com.example.weatherforecast.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FavouriteDetailsFragment : Fragment() {
    lateinit var favDetailsViewModelFactory: MyViewModelFactory
    lateinit var favDetailsViewModel: MyViewModel
    lateinit var favHourlyAdapter: FavHourlyAdapter
    lateinit var favDailyAdapter: FavDailyAdapter
    //lateinit var welcome: Welcome

    private var _binding: FragmentFavouriteDetailsBinding? =null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favDetailsViewModelFactory= MyViewModelFactory(
            Repository.getInstance(
                RemoteSource.getINSTANCE(),
                LocalSource(requireActivity())
            ))
        favDetailsViewModel =
            ViewModelProvider(requireActivity(),favDetailsViewModelFactory).get(MyViewModel::class.java)
        // Inflate the layout for this fragment
        _binding = FragmentFavouriteDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pd = ProgressDialog(requireActivity())
        favHourlyAdapter= FavHourlyAdapter()
        binding.hourlyRv.apply{
            this.adapter=favHourlyAdapter
            layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        }
        favDailyAdapter= FavDailyAdapter()
        binding.dailyRv.apply {
            this.adapter=favDailyAdapter
            layoutManager=LinearLayoutManager(context)
        }

        favDetailsViewModel.currentObjFav.observe(viewLifecycleOwner){
            binding.homeCountryTxt.text=it.timezone
            binding.homeTempTxt.text=Math.ceil(it.current.temp).toInt().toString()+"°C"
            binding.homeDescTxt.text=it.current.weather.get(0).description
            Glide.with(requireActivity()).load("https://openweathermap.org/img/wn/${it.current.weather.get(0).icon}@2x.png").into(binding.homeIconImg)

            favHourlyAdapter.submitList(it.hourly)
            favHourlyAdapter.notifyDataSetChanged()

            favDailyAdapter.submitList(it.daily)
            favDailyAdapter.notifyDataSetChanged()

            binding.humidityTxt.text=it.current.humidity.toString()+"%"
            binding.dewPointTxt.text="The dew point is ${Math.ceil(it.current.dew_point)} right now"
            binding.windSpeedTxt.text=it.current.wind_speed.toString()+" km/h"
            binding.pressureTxt.text=it.current.pressure.toString()+" hPa"
            binding.cloudsTxt.text=it.current.clouds.toString()
        }
        lifecycleScope.launch {
            favDetailsViewModel.apiObjForFav.collectLatest {
                when (it) {
                    is APIState.Loading -> {

                        pd.setMessage("loading")
                        pd.show()


                    }
                    is APIState.Success -> {
                        pd.dismiss()
                        if (it.data.body() != null) {
                           //welcome= it.data.body()!!
                            binding.homeCountryTxt.text=it.data.body()!!.timezone
                            binding.homeTempTxt.text=Math.ceil(it.data.body()!!.current.temp).toInt().toString()+"°C"
                            binding.homeDescTxt.text=it.data.body()!!.current.weather.get(0).description
                            Glide.with(requireActivity()).load("https://openweathermap.org/img/wn/${it.data.body()!!.current.weather.get(0).icon}@2x.png").into(binding.homeIconImg)

                            favHourlyAdapter.submitList(it.data.body()!!.hourly)
                            favHourlyAdapter.notifyDataSetChanged()

                            favDailyAdapter.submitList(it.data.body()!!.daily)
                            favDailyAdapter.notifyDataSetChanged()

                            binding.humidityTxt.text=it.data.body()!!.current.humidity.toString()+"%"
                            binding.dewPointTxt.text="The dew point is ${Math.ceil(it.data.body()!!.current.dew_point)} right now"
                            binding.windSpeedTxt.text=it.data.body()!!.current.wind_speed.toString()+" km/h"
                            binding.pressureTxt.text=it.data.body()!!.current.pressure.toString()+" hPa"
                            binding.cloudsTxt.text=it.data.body()!!.current.clouds.toString()

                        }
                    }
                    else -> {
                        pd.dismiss()
                        Toast.makeText(
                            requireActivity(),
                            "Check your connection",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }

        }

    }


