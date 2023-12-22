package com.example.ppapb_uas.Welcome

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ppapb_uas.R

// Kelas adapter untuk mengelola tampilan pager pada onboarding.
class OnBoardingViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val context: Context
) :
    FragmentStateAdapter(fragmentActivity) {

    // Fungsi untuk membuat dan mengembalikan fragmen pada posisi tertentu.
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            // Mengembalikan fragmen pertama dengan judul, deskripsi, dan gambar yang sesuai.
            0 -> WelcomeFragment.newInstance(
                context.resources.getString(R.string.title_onboarding_1),
                context.resources.getString(R.string.description_onboarding_1),
                R.drawable.welcome1
            )
            // Mengembalikan fragmen kedua dengan judul, deskripsi, dan gambar yang sesuai.
            1 -> WelcomeFragment.newInstance(
                context.resources.getString(R.string.title_onboarding_2),
                context.resources.getString(R.string.description_onboarding_2),
                R.drawable.welcome2
            )
            // Mengembalikan fragmen ketiga dengan judul, deskripsi, dan gambar yang sesuai.
            else -> WelcomeFragment.newInstance(
                context.resources.getString(R.string.title_onboarding_3),
                context.resources.getString(R.string.description_onboarding_3),
                R.drawable.welcome3
            )
        }
    }

    // Fungsi untuk mendapatkan jumlah total item di tampilan pager.
    override fun getItemCount(): Int {
        return 3
    }
}
