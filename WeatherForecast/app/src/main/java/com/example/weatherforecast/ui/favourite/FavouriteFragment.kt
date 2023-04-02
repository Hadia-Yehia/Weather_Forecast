package com.example.weatherforecast.ui.favourite

import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.DataBase.LocalSource
import com.example.weatherforecast.InitialFragmentDirections
import com.example.weatherforecast.Model.Repository
import com.example.weatherforecast.MyViewModel
import com.example.weatherforecast.MyViewModelFactory
import com.example.weatherforecast.Network.RemoteSource
import com.example.weatherforecast.databinding.FragmentFavouriteBinding


class FavouriteFragment : Fragment() {
    lateinit var favViewModelFactory: MyViewModelFactory
    lateinit var favViewModel: MyViewModel
    lateinit var favouriteAdapter: FavouriteAdapter

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
        binding.fabBtn.setOnClickListener {
            val action1=FavouriteFragmentDirections.actionNavFavouriteToMapsFragment(true)
            findNavController().navigate(action1)
        }
        favouriteAdapter= FavouriteAdapter{
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
        favViewModel.favObj.observe(viewLifecycleOwner){
            favouriteAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun isOnline(): Boolean {
        val cm = context?.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return if (activeNetwork != null) {true}else{false}}
}

