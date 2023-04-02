package com.example.weatherforecast.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import com.google.android.gms.location.*
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherforecast.DataBase.LocalSource
import com.example.weatherforecast.Model.Repository
import com.example.weatherforecast.Model.Welcome
import com.example.weatherforecast.MyViewModel
import com.example.weatherforecast.MyViewModelFactory
import com.example.weatherforecast.Network.RemoteSource
import com.example.weatherforecast.databinding.FragmentHomeBinding
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import java.util.*

const val PERMISSION_ID=400
class HomeFragment : Fragment() {
    lateinit var homeViewModelFactory: MyViewModelFactory
    lateinit var homeViewModel: MyViewModel
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var dailyAdapter: DailyAdapter
    lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    lateinit var lat:String
    lateinit var lon:String
    lateinit var welcome: Welcome
    val args:HomeFragmentArgs by navArgs()

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModelFactory= MyViewModelFactory(Repository.getInstance(RemoteSource.getINSTANCE(),LocalSource(requireActivity())))
        homeViewModel =
            ViewModelProvider(requireActivity(),homeViewModelFactory).get(MyViewModel::class.java)

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

        homeViewModel.apiObj.observe(viewLifecycleOwner){

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

            welcome=it
            binding.addToFav.setOnClickListener {
                welcome.flag=true
                homeViewModel.insertPlaceInRoom(welcome) }
        }

    }
    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        if(checkPermissions()){
            if(isLocationEnabled()){
                requestNewLocationData()
            }
            else{
                Toast.makeText(context,"turn on location", Toast.LENGTH_SHORT).show()
                val intent= Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)

            }
        }
        else{
            requestPermission()
        }
    }
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID)

    }
    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        mFusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken =
                CancellationTokenSource().token
            override fun isCancellationRequested(): Boolean =false

        }
        ).addOnSuccessListener { location: Location? ->
            if (location == null)
                Toast.makeText(requireContext(), "Cannot get location.", Toast.LENGTH_SHORT).show()
            else {
                lat=location!!.latitude.toString()
                lon=location!!.longitude.toString()
                homeViewModel.getDataFromApi(lat,lon)
                Log.i("tag","loc"+lat)
            } }
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE)!! as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }
    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )== PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )== PackageManager.PERMISSION_GRANTED
    }
    override fun onResume() {
        super.onResume()
        if(args.map){
            Log.i("tag","hi"+args.lat+" "+args.lon)
            homeViewModel.getDataFromApi(args.lat,args.lon)
        }
        else{mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(requireActivity())
            getLastLocation()}


    }
    override fun onDestroyView() {
                super.onDestroyView()
                _binding = null
            }
        }


