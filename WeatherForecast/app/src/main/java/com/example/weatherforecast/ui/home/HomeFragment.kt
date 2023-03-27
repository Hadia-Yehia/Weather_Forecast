package com.example.weatherforecast.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import com.google.android.gms.location.*
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherforecast.Model.Repository
import com.example.weatherforecast.Network.RemoteSource
import com.example.weatherforecast.databinding.FragmentHomeBinding
import com.google.android.gms.location.LocationRequest
import java.text.SimpleDateFormat
import java.util.*

const val PERMISSION_ID=400
class HomeFragment : Fragment() {
    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var homeViewModel: HomeViewModel
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var dailyAdapter: DailyAdapter
    lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    lateinit var lat:String
    lateinit var lon:String

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
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if(checkPermissions()){
            if(isLocationEnabled()){
                requestNewLocationData()

            }
            else{
                Toast.makeText(requireActivity(),"turn on location", Toast.LENGTH_SHORT).show()
                val intent= Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }
        else{
            requestPermissions()
        }

    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )==PackageManager.PERMISSION_GRANTED

    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)

    }





    private val mLocatiobCallback:LocationCallback=object :LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            val mLastLocation: Location? = p0.lastLocation
            if (mLastLocation != null) {
                Log.i("tag","d5lt"+mLastLocation)
                lat=mLastLocation.latitude.toString()
                lon=mLastLocation.longitude.toString()
                homeViewModel.getDataFromApi(lat,lon)
            }

        }}



            @SuppressLint("MissingPermission")
            private fun requestNewLocationData() {
                val mLocationRequest = LocationRequest()
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                mLocationRequest.setInterval(0)

                mFusedLocationProviderClient.requestLocationUpdates(
                    mLocationRequest, mLocatiobCallback,
                    Looper.myLooper()
                )
            }


            fun requestPermissions() {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ), PERMISSION_ID
                )

            }




            override fun onDestroyView() {
                super.onDestroyView()
                _binding = null
            }

    override fun onResume() {
        super.onResume()
        mFusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(requireActivity())
        getLastLocation()
    }

        }


