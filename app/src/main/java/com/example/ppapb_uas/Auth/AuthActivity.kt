package com.example.ppapb_uas.Auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.ppapb_uas.R
import com.example.ppapb_uas.databinding.ActivityAuthBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Menghubungkan layout menggunakan ViewBinding
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mendapatkan referensi ke TabLayout dan ViewPager dari layout
        val tabLayout: TabLayout = binding.tabLayout
        val viewPager: ViewPager2 = binding.viewPager

        // Membuat adapter untuk mengelola fragmen Login dan Signup
        val adapter = AuthViewPagerAdapter(this)
        viewPager.adapter = adapter

        // Menghubungkan TabLayout dan ViewPager dengan mediator
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            // Menetapkan teks pada setiap tab berdasarkan posisi
            tab.text = when (position) {
                0 -> "Login"
                1 -> "Signup"
                else -> ""
            }
        }.attach()
    }
}
