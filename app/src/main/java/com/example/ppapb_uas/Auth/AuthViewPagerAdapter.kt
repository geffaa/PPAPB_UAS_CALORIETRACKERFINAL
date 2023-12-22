package com.example.ppapb_uas.Auth

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class AuthViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    // Jumlah halaman pada ViewPager
    override fun getItemCount(): Int = 2

    // Membuat Fragment sesuai dengan posisi halaman
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LoginFragment()  // Halaman pertama menggunakan LoginFragment
            1 -> SignUpFragment() // Halaman kedua menggunakan SignUpFragment
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
