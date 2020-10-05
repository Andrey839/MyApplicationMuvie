package com.myapp.myapplicationmuvie

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.myapp.myapplicationmuvie.fragments.KEY_NAME
import com.myapp.myapplicationmuvie.fragments.KEY_URI
import com.myapp.myapplicationmuvie.work.WorkTime
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header_main_menu.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private val scopeCoroutines = CoroutineScope(Dispatchers.Default)

    private var sharedPreference: SharedPreferences? = null

    private var listener: SharedPreferences.OnSharedPreferenceChangeListener? = null

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val constrains = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) setRequiresDeviceIdle(true)
            }
            .build()

        delayedInit(constrains)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController
        findViewById<NavigationView>(R.id.navigation_view).setupWithNavController(navController)
        val myDrawer = findViewById<DrawerLayout>(R.id.myDrawer)
        NavigationUI.setupActionBarWithNavController(this, navController, myDrawer)

        sharedPreference = this.getPreferences(Context.MODE_PRIVATE)
        sharedPreference?.registerOnSharedPreferenceChangeListener(listener)

        val header = navigation_view.getHeaderView(0)
        val nameUser = header.findViewById<TextView>(R.id.avatar)
        val avatarUser = header.findViewById<ImageView>(R.id.avatar_view)

        nameUser.text = sharedPreference?.getString(KEY_NAME, "???")
        Glide.with(this).load(sharedPreference?.getString(KEY_URI, ""))
            .error(resources.getDrawable(R.drawable.ic_baseline_broken_image))
            .circleCrop().into(avatarUser)
    }

    private fun delayedInit(constraints: Constraints) {
        scopeCoroutines.launch {
            val refreshDataWrk =
                PeriodicWorkRequestBuilder<WorkTime>(1, TimeUnit.DAYS).setConstraints(constraints)
                    .build()

            WorkManager.getInstance(this@MainActivity).enqueueUniquePeriodicWork(
                WorkTime.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                refreshDataWrk
            )
        }
    }

    override fun onSupportNavigateUp() = NavigationUI.navigateUp(
        Navigation.findNavController(this, R.id.fragmentContainer),
        findViewById<DrawerLayout>(R.id.myDrawer)
    )

    override fun onBackPressed() {
        if (myDrawer.isDrawerOpen(GravityCompat.START)) {
            myDrawer.closeDrawer(GravityCompat.START)
        } else super.onBackPressed()
    }

    override fun onResume() {

        val header = navigation_view.getHeaderView(0)
        val nameUser = header.avatar
        val avatarUser = header.avatar_view

        listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == KEY_NAME) nameUser.text = sharedPreferences.getString(key, "???")
            else Glide.with(this).load(sharedPreferences.getString(key, ""))
                .error(resources.getDrawable(R.drawable.ic_baseline_broken_image))
                .circleCrop().into(avatarUser)
        }

        sharedPreference?.registerOnSharedPreferenceChangeListener(listener)

        super.onResume()
    }

    override fun onStop() {
        sharedPreference?.unregisterOnSharedPreferenceChangeListener(listener)
        super.onStop()
    }

}