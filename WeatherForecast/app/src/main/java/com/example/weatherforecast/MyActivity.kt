package com.example.weatherforecast

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherforecast.Network.RemoteSource
import com.example.weatherforecast.databinding.ActivityMyBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*

class MyActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMyBinding
    var layoutDirection:Int?= null
    companion object{
        var isNotOpen=true;
    }
    override fun attachBaseContext(newBase: Context?) {
        if (newBase != null && isNotOpen) {
            MySharedPreference.getInstance(newBase)
            setLocal(MySharedPreference.getLanguage(), newBase)
            isNotOpen=false
        }
        super.attachBaseContext(newBase)
    }

    private fun setLocal(language: String, newBase: Context) {

            val local = Locale(language)
            Locale.setDefault(local)
            val config = Configuration()
            config.setLocale(local)
            newBase.resources.updateConfiguration(
                config,
                newBase.resources.displayMetrics
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
        }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (layoutDirection!=null)
            window.decorView.layoutDirection = layoutDirection!!
        binding = ActivityMyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMy.toolbar)


       /* binding.appBarMy.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_my)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_favourite, R.id.nav_alert, R.id.settingsFragment,R.id.mapsFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_my)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    

}