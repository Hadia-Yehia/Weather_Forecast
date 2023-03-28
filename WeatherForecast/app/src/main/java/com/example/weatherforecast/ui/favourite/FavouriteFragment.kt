package com.example.weatherforecast.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.DataBase.LocalSource
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
        favouriteAdapter= FavouriteAdapter{favViewModel.getFromFav(it)}
       binding.favRv.apply {
           this.adapter=favouriteAdapter
           layoutManager=LinearLayoutManager(context)
       }
        favViewModel.getDataFromRoom()
        favViewModel.favData.observe(viewLifecycleOwner){
            favouriteAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}