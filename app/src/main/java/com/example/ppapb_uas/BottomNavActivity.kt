package com.example.ppapb_uas

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import nl.joery.animatedbottombar.AnimatedBottomBar
import com.example.ppapb_uas.databinding.ActivityBottomNavBinding
import android.Manifest

class BottomNavActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottomNavBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomNavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_bottom_navigation) as NavHostFragment
        navController = navHostFragment.navController

        val bottomBar = findViewById<AnimatedBottomBar>(R.id.nav_view)
        bottomBar.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                when (newTab.id) {
                    R.id.navigation_home -> navController.navigate(R.id.navigation_home)
                    R.id.navigation_history -> navController.navigate(R.id.navigation_history)
                    R.id.navigation_profiles -> navController.navigate(R.id.navigation_profiles)
                }
            }

            override fun onTabReselected(index: Int, tab: AnimatedBottomBar.Tab) {
                // Handle reselected tab if needed
            }
        })
    }

    fun createNotification() {
        // Notification channel is required for Android Oreo (API level 26) and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "YOUR_CHANNEL_ID",
                "YOUR_CHANNEL_NAME",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val builder = NotificationCompat.Builder(this, "YOUR_CHANNEL_ID")
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle("Calorie Tracker")
            .setContentText("Target Harian Anda Sudah Terpenuhi")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Show the notification
        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@BottomNavActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(1, builder.build())
        }
    }
}