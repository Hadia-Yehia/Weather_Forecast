package com.example.weatherforecast

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class InitialFragment : Fragment() {

    private var selectedMethodIndex: Int = 0
    private val methods = arrayOf("GPS","Map")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_initial, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showAlertDialog()
    }
    fun showAlertDialog() {

        var selectedMethods = methods[selectedMethodIndex]
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Location")
            .setSingleChoiceItems(methods, selectedMethodIndex) { dialog_, which ->
                selectedMethodIndex = which
                selectedMethods = methods[which]
            }
            .setPositiveButton("Ok") { dialog, which ->
                if (selectedMethods.equals("GPS")){
                    val action=InitialFragmentDirections.actionInitialFragmentToNavHome()
                    findNavController().navigate(action)

                }
                else if(selectedMethods.equals("Map")){
                    val action=InitialFragmentDirections.actionInitialFragmentToMapsFragment()
                    findNavController().navigate(action)
                }

                Toast.makeText(requireActivity(), "$selectedMethods Selected", Toast.LENGTH_SHORT)
                    .show()
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .show()}
}