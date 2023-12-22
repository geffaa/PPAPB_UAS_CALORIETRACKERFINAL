package com.example.ppapb_uas.Welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import com.example.ppapb_uas.Auth.AuthActivity
import com.example.ppapb_uas.R
import com.example.ppapb_uas.databinding.ActivityWelcomeBinding
import com.google.android.material.tabs.TabLayoutMediator

// Activity untuk menampilkan layar selamat datang (onboarding).
class WelcomeActivity : AppCompatActivity() {
    private lateinit var mViewPager: ViewPager2
    private lateinit var btnCreateAccount: Button

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        btnCreateAccount = binding.btnCreateAccount
        // Mengatur onClickListener untuk tombol membuat akun.
        btnCreateAccount.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }

        mViewPager = findViewById(R.id.viewPager)
        // Mengatur adapter untuk ViewPager dengan menggunakan OnBoardingViewPagerAdapter.
        mViewPager.adapter = OnBoardingViewPagerAdapter(this, this)
        // Menyematkan TabLayout ke ViewPager.
        TabLayoutMediator(binding.pageIndicator, mViewPager) { _, _ -> }.attach()
        mViewPager.offscreenPageLimit = 1
    }
}
