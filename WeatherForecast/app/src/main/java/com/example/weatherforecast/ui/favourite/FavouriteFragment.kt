package com.example.weatherforecast.ui.favourite

import com.example.weatherforecast.ui.favourite.FavouriteAdapter
import com.example.weatherforecast.ui.favourite.FavouriteFragmentArgs
import com.example.weatherforecast.ui.favourite.FavouriteFragmentDirections
import android.app.ProgressDialog
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.DataBase.LocalSource
import com.example.weatherforecast.DataBase.RoomState
import com.example.weatherforecast.InitialFragmentDirections
import com.example.weatherforecast.Model.Repository
import com.example.weatherforecast.Model.Welcome
import com.example.weatherforecast.MyViewModel
import com.example.weatherforecast.MyViewModelFactory
import com.example.weatherforecast.Network.APIState
import com.example.weatherforecast.Network.RemoteSource
import com.example.weatherforecast.databinding.FragmentFavouriteBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FavouriteFragment : Fragment(),onFavDeleteClick {
    lateinit var favViewModelFactory: MyViewModelFactory
    lateinit var favViewModel: MyViewModel
    lateinit var favouriteAdapter: FavouriteAdapter
    val args: FavouriteFragmentArgs by navArgs()
    var mapToogle:Boolean=false

    private var _binding: FragmentFavouriteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        favViewModelFactory= MyViewModelFactory(
            Repository.getInstance(
                RemoteSource.getINSTANCE(),
                LocalSource(requireActivity())
            ))
        favViewModel =
            ViewModelProvider(requireActivity(),favViewModelFactory).get(MyViewModel::class.java)
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pd = ProgressDialog(requireActivity())
            lifecycleScope.launch {
                favViewModel.apiObj.collectLatest {
                    when (it) {
                        is APIState.Loading -> {

                            pd.setMessage("loading")
                            pd.show()
                        }
                        is APIState.Success -> {
                            pd.dismiss()
                            if (it.data.body() != null) {
                                if(mapToogle) {
                                favViewModel.insertPlaceInRoom(it.data.body()!!)
                                    mapToogle=false
                            }}
                        }
                        else -> {
                            pd.dismiss()
                            Toast.makeText(
                                requireActivity(),
                                "Check your connection",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

        }
        binding.fabBtn.setOnClickListener {
            mapToogle=true
            val action1= FavouriteFragmentDirections.actionNavFavouriteToMapsFragment(true)
            findNavController().navigate(action1)

        }
        favouriteAdapter= FavouriteAdapter(this){
            if(isOnline()){
                favViewModel.getFromApiForFav(it.lat.toString(),it.lon.toString())
            }
            else{
                favViewModel.getFromFav(it)
            }
            val action= FavouriteFragmentDirections.actionNavFavouriteToFavouriteDetailsFragment()
            findNavController().navigate(action)

            }
       binding.favRv.apply {
           this.adapter=favouriteAdapter
           layoutManager=LinearLayoutManager(context)
       }
        favViewModel.getDataFromRoom()
        lifecycleScope.launch {
            favViewModel.favObj.collectLatest {
                when(it){
                    is RoomState.Loading ->{

                        pd.setMessage("loading")
                        pd.show()


                    }
                    is RoomState.Success->{
                        pd.dismiss()
                        favouriteAdapter.submitList(it.data)
                        favouriteAdapter.notifyDataSetChanged()
                    }
                    else->{
                        pd.dismiss()
                        Toast.makeText(requireActivity(),"Can't handle your request", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        if (isOnline()){
            if(args!=null){
                favViewModel.getDataFromApi(args.lat,args.lon)

            }
        }
    }
    private fun isOnline(): Boolean {
        val cm = context?.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return if (activeNetwork != null) {true}else{false}}

    override fun deleteFav(welcome: Welcome) {
        favViewModel.deletePlaceFromRoom(welcome)
    }
}

