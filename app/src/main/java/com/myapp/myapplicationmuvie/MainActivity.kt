package com.myapp.myapplicationmuvie

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.myapp.myapplicationmuvie.work.WorkTime
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val scopeCoroutines = CoroutineScope(Dispatchers.Default)

    private var sharedPreference: SharedPreferences? = null

    private var listener: SharedPreferences.OnSharedPreferenceChangeListener? = null

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
        val navController = navHostFragment.navController
        findViewById<NavigationView>(R.id.navigation_view).setupWithNavController(navController)
        val myDrawer = findViewById<DrawerLayout>(R.id.myDrawer)
        NavigationUI.setupActionBarWithNavController(this, navController, myDrawer)

        sharedPreference = this.getPreferences(Context.MODE_PRIVATE)

        val header = navigation_view.getHeaderView(0)
        val nameUser = header.findViewById<TextView>(R.id.avatar)
        val avatarUser = header.findViewById<ImageView>(R.id.avatar_view)
        nameUser.text = sharedPreference?.getString("name", "???")
        Glide.with(avatarUser.context).load(sharedPreference?.getString("uri", ""))
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
        val nameUser = header.findViewById<TextView>(R.id.avatar)
        val avatarUser = header.findViewById<ImageView>(R.id.avatar_view)

        listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == "name") nameUser.text = sharedPreferences.getString(key, "???")
            else Glide.with(avatarUser.context).load(sharedPreferences.getString("uri", ""))
                .error(resources.getDrawable(R.drawable.ic_baseline_broken_image))
                .circleCrop().into(avatarUser)
        }

        sharedPreference?.registerOnSharedPreferenceChangeListener(listener)

        super.onResume()
    }

    override fun onDestroy() {
        sharedPreference?.unregisterOnSharedPreferenceChangeListener(listener)
        super.onDestroy()
    }

}