package com.example.weatherforecast.ui.alert

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkManager
import com.example.weatherforecast.DataBase.LocalSource
import com.example.weatherforecast.DataBase.RoomState
import com.example.weatherforecast.Model.Repository
import com.example.weatherforecast.MyViewModel
import com.example.weatherforecast.MyViewModelFactory
import com.example.weatherforecast.Network.RemoteSource
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentAlertBinding
import com.example.weatherforecast.ui.favourite.FavouriteAdapter
import com.example.weatherforecast.ui.favourite.FavouriteFragmentDirections
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AlertFragment : Fragment() {

    private var _binding: FragmentAlertBinding? = null
    private val binding get() = _binding!!
    lateinit var alertAdapter: Adapter
    lateinit var alertViewModelFactory: MyViewModelFactory
    lateinit var alertViewModel: MyViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        alertViewModelFactory = MyViewModelFactory(
            Repository.getInstance(
                RemoteSource.getINSTANCE(),
                LocalSource(requireActivity())
            )
        )
        alertViewModel =
            ViewModelProvider(requireActivity(), alertViewModelFactory).get(MyViewModel::class.java)

        _binding = FragmentAlertBinding.inflate(inflater, container, false)

        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pd = ProgressDialog(requireActivity())

        alertAdapter = Adapter() {
            alertViewModel.deleteAlertFromRoom(it)
            WorkManager.getInstance().cancelAllWorkByTag("${it.id}")
        }
        binding.alertRv.apply {
            this.adapter = alertAdapter
            layoutManager = LinearLayoutManager(context)
        }
        alertViewModel.getAlertsFromRoom()

        lifecycleScope.launch {
            alertViewModel.alertObj.collectLatest {
                when (it) {
                    is RoomState.Loading -> {

                        pd.setMessage("loading")
                        pd.show()


                    }
                    is RoomState.SuccessAlert -> {
                        pd.dismiss()
                        alertAdapter.submitList(it.data)
                        alertAdapter.notifyDataSetChanged()
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



        binding.alertFab.setOnClickListener {
            AlertDialogFragment().show(requireActivity().supportFragmentManager, "AlertDialog")
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}