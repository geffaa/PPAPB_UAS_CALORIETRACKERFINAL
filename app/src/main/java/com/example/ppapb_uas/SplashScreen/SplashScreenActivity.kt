package com.example.ppapb_uas.SplashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ppapb_uas.R
import com.example.ppapb_uas.Welcome.WelcomeActivity

// SplashScreenActivity adalah aktivitas pertama yang muncul saat aplikasi dijalankan.
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Menggunakan handler untuk menunda navigasi ke aktivitas selanjutnya (WelcomeActivity) setelah jangka waktu tertentu.
        val splashDuration = 3000L // Durasi splash screen dalam milidetik
        val intent = Intent(this, WelcomeActivity::class.java)

        val handler = android.os.Handler()
        handler.postDelayed({
            startActivity(intent)
            finish()
        }, splashDuration)
    }
}
