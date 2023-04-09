package com.example.weatherforecast.ui.settings

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.example.weatherforecast.MySharedPreference

import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentFavouriteBinding
import com.example.weatherforecast.databinding.FragmentSettingsBinding
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        if (MySharedPreference.getLanguage() == "en")
            binding.languageGroup.check(R.id.english)
        else
            binding.languageGroup.check(R.id.arabic)

        if (MySharedPreference.getWeatherFromMap())
            binding.locationGroup.check(R.id.maps)
        else
            binding.locationGroup.check(R.id.gps)

        binding.locationGroup.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
            if (i == R.id.map)
                MySharedPreference.setWeatherFromMap(true)
            else
                MySharedPreference.setWeatherFromMap(false)
        }



        when (MySharedPreference.getUnits()) {
            "default" -> {
                binding.unitsGroup.check(R.id.kelvin)
            }
            "metric" -> {
                binding.unitsGroup.check(R.id.celsius)
            }
            "imperial" -> {
                binding.unitsGroup.check(R.id.fahrenheit)
            }
        }

        binding.languageGroup.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
            if (i == R.id.arabic && MySharedPreference.getLanguage()=="en") {
                MySharedPreference.setLanguage("ar")
                setLocal("ar")
            } else if (i == R.id.english && MySharedPreference.getLanguage()=="ar") {
                MySharedPreference.setLanguage("en")
                setLocal("en")
            }
        }
        binding.unitsGroup.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
            when (i) {
                R.id.kelvin -> {
                    MySharedPreference.setUnit("default")
                }
                R.id.celsius -> {
                    MySharedPreference.setUnit("metric")
                }
                R.id.fahrenheit -> {
                    MySharedPreference.setUnit("imperial")
                }
            }
        }
        val root: View = binding.root
        return root
    }

    private fun setLocal(lang: String) {
        val local = Locale(lang)
        Locale.setDefault(local)
        val config = Configuration()
        config.setLocale(local)
        requireActivity().baseContext.resources.updateConfiguration(
            config,
            requireActivity().baseContext.resources.displayMetrics
        )

        // Determine layout direction based on selected language
        val layoutDirection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (TextUtils.getLayoutDirectionFromLocale(local) == View.LAYOUT_DIRECTION_RTL) {
                View.LAYOUT_DIRECTION_RTL
            } else {
                View.LAYOUT_DIRECTION_LTR
            }
        } else {
            View.LAYOUT_DIRECTION_LTR
        }

        // Set layout direction on the root view
        requireActivity().window.decorView.layoutDirection = layoutDirection
        this.requireActivity().recreate()
    }


}