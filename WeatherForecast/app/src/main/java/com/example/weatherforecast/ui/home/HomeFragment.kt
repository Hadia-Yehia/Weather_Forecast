package com.example.weatherforecast.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import com.google.android.gms.location.*
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherforecast.DataBase.LocalSource
import com.example.weatherforecast.DataBase.RoomState
import com.example.weatherforecast.Model.Current
import com.example.weatherforecast.Model.HomeModel
import com.example.weatherforecast.Model.Repository
import com.example.weatherforecast.Model.Welcome
import com.example.weatherforecast.MySharedPreference
import com.example.weatherforecast.MyViewModel
import com.example.weatherforecast.MyViewModelFactory
import com.example.weatherforecast.Network.APIState
import com.example.weatherforecast.Network.RemoteSource
import com.example.weatherforecast.databinding.FragmentHomeBinding
import com.example.weatherforecast.ui.favourite.FavouriteFragmentDirections
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

const val PERMISSION_ID = 400

class HomeFragment : Fragment() {
    lateinit var homeViewModelFactory: MyViewModelFactory
    lateinit var homeViewModel: MyViewModel
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var dailyAdapter: DailyAdapter
    lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    lateinit var lat: String
    lateinit var lon: String

    //lateinit var welcome: Welcome
    val args: HomeFragmentArgs by navArgs()

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModelFactory = MyViewModelFactory(
            Repository.getInstance(
                RemoteSource.getINSTANCE(),
                LocalSource(requireActivity())
            )
        )
        homeViewModel =
            ViewModelProvider(requireActivity(), homeViewModelFactory).get(MyViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pd = ProgressDialog(requireActivity())



        hourlyAdapter = HourlyAdapter()
        binding.hourlyRv.apply {
            this.adapter = hourlyAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        dailyAdapter = DailyAdapter()
        binding.dailyRv.apply {
            this.adapter = dailyAdapter
            layoutManager = LinearLayoutManager(context)
        }
        if (isOnline()) {
            lifecycleScope.launch {
                homeViewModel.apiObj.collectLatest {
                    when (it) {
                        is APIState.Loading -> {

                            pd.setMessage("loading")
                            pd.show()
                        }
                        is APIState.Success -> {
                            pd.dismiss()
                            if (it.data.body() != null) {
//                                welcome = it.data.body()!!
                                binding.homeCountryTxt.text = it.data.body()!!.timezone
                                if (MySharedPreference.getUnits().equals("metric")) {
                                    binding.homeTempTxt.text =
                                        Math.ceil(it.data.body()!!.current.temp).toInt()
                                            .toString() + "°C"
                                } else if (MySharedPreference.getUnits().equals("imperial")) {
                                    binding.homeTempTxt.text =
                                        Math.ceil(it.data.body()!!.current.temp).toInt()
                                            .toString() + "°F"
                                } else {
                                    binding.homeTempTxt.text =
                                        Math.ceil(it.data.body()!!.current.temp).toInt()
                                            .toString() + "°K"
                                }

                                binding.homeDescTxt.text =
                                    it.data.body()!!.current.weather.get(0).description
                                binding.time.text =
                                    getCurrentTime(it.data.body()!!.current.dt.toInt())
                                binding.date.text =
                                    getCurrentDay(it.data.body()!!.current.dt.toInt())
                                Glide.with(requireActivity())
                                    .load(
                                        "https://openweathermap.org/img/wn/${
                                            it.data.body()!!.current.weather.get(
                                                0
                                            ).icon
                                        }@2x.png"
                                    )
                                    .into(binding.homeIconImg)

                                hourlyAdapter.submitList(it.data.body()!!.hourly)
                                hourlyAdapter.notifyDataSetChanged()

                                dailyAdapter.submitList(it.data.body()!!.daily)
                                dailyAdapter.notifyDataSetChanged()

                                binding.humidityTxt.text =
                                    it.data.body()!!.current.humidity.toString() + "%"
                                binding.dewPointTxt.text =
                                    "The dew point is ${Math.ceil(it.data.body()!!.current.dew_point)} right now"
                                binding.windSpeedTxt.text =
                                    it.data.body()!!.current.wind_speed.toString() + " km/h"
                                binding.pressureTxt.text =
                                    it.data.body()!!.current.pressure.toString() + " hPa"
                                binding.cloudsTxt.text = it.data.body()!!.current.clouds.toString()
//                                it.data.body()!!.flag = false
//                                Log.i("tag", "elmoshkela" + it.data.body()!!.flag)
                                //   homeViewModel.deleteHome()
                                //  homeViewModel.insertPlaceInRoom(it.data.body()!!)
                                var homeModel: HomeModel = HomeModel(
                                    it.data.body()!!
                                )
                                homeViewModel.insertHome(homeModel)


                                var welcome = it.data.body()!!
                                binding.addToFav.setOnClickListener {
                                    homeViewModel.insertPlaceInRoom(welcome)
                                }
                            }
                        }


                        else -> {
                            pd.dismiss()
                            Toast.makeText(
                                requireActivity(),
                                "Can't handle your request",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }


        } else {
            lifecycleScope.launch {
                homeViewModel.currentObjHome.collectLatest {
                    when (it) {
                        is RoomState.Loading -> {

                            pd.setMessage("loading")
                            pd.show()


                        }
                        is RoomState.SuccessHome -> {
                            pd.dismiss()

                            if (it.data != null) {
                                binding.homeCountryTxt.text = it.data!!.welcome.timezone
                                binding.homeTempTxt.text =
                                    Math.ceil(it.data.welcome.current.temp).toInt()
                                        .toString() + "°K"
                                binding.homeDescTxt.text =
                                    it.data.welcome.current.weather.get(0).description
                                binding.time.text =
                                    getCurrentTime(it.data.welcome.current.dt.toInt())
                                binding.date.text =
                                    getCurrentDay(it.data.welcome.current.dt.toInt())
                                Glide.with(requireActivity())
                                    .load(
                                        "https://openweathermap.org/img/wn/${
                                            it.data.welcome.current.weather.get(
                                                0
                                            ).icon
                                        }@2x.png"
                                    )
                                    .into(binding.homeIconImg)

                                hourlyAdapter.submitList(it.data.welcome.hourly)
                                hourlyAdapter.notifyDataSetChanged()

                                dailyAdapter.submitList(it.data.welcome.daily)
                                dailyAdapter.notifyDataSetChanged()

                                binding.humidityTxt.text =
                                    it.data.welcome.current.humidity.toString() + "%"
                                binding.dewPointTxt.text =
                                    "The dew point is ${Math.ceil(it.data.welcome.current.dew_point)} right now"
                                binding.windSpeedTxt.text =
                                    it.data.welcome.current.wind_speed.toString() + " km/h"
                                binding.pressureTxt.text =
                                    it.data.welcome.current.pressure.toString() + " hPa"
                                binding.cloudsTxt.text = it.data.welcome.current.clouds.toString()

                                // it.data.flag = false
                                //Log.i("tag", "elmoshkela" + it.data.flag)
                                // homeViewModel.deleteHome()
                                homeViewModel.insertHome(it.data)


                                var welcome = it.data.welcome
                                binding.addToFav.setOnClickListener {
                                    homeViewModel.insertPlaceInRoom(welcome)
                                }

                            }
                        }
                        else -> {
                            pd.dismiss()
                            Toast.makeText(
                                requireActivity(),
                                "Can't handle your request",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                requestNewLocationData()
            } else {
                Toast.makeText(context, "turn on location", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)

            }
        } else {
            requestPermission()
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
        )

    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        mFusedLocationProviderClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken =
                    CancellationTokenSource().token

                override fun isCancellationRequested(): Boolean = false

            }
        ).addOnSuccessListener { location: Location? ->
            if (location == null)
                Toast.makeText(requireContext(), "Cannot get location.", Toast.LENGTH_SHORT).show()
            else {
                lat = location!!.latitude.toString()
                lon = location!!.longitude.toString()
                homeViewModel.getDataFromApi(lat, lon)
                Log.i("tag", "loc" + lat)
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE)!! as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onResume() {
        super.onResume()

        if (isOnline())
            if (args.map) {
                homeViewModel.getDataFromApi(args.lat, args.lon)
            } else {
                mFusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(requireActivity())
                getLastLocation()
            }
        else {
            homeViewModel.getHome()
        }

    }

    private fun isOnline(): Boolean {
        val cm =
            context?.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return if (activeNetwork != null) {
            true
        } else {
            false
        }
    }

    fun getCurrentDay(dt: Int): String {
        var date = Date(dt * 1000L)
        var sdf = SimpleDateFormat("d")
        sdf.timeZone = TimeZone.getDefault()
        var formatedData = sdf.format(date)
        var intDay = formatedData.toInt()
        var calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, intDay)
        var format = SimpleDateFormat("EEEE")
        return format.format(calendar.time)
    }

    private fun getCurrentTime(dt: Int): String {
        var date = Date(dt * 1000L)
        var sdf = SimpleDateFormat("hh:mm a")
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


