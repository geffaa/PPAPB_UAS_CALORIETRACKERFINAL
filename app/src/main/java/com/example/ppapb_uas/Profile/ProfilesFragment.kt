package com.example.ppapb_uas.Profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.ppapb_uas.Auth.AuthActivity
import com.example.ppapb_uas.R
import com.example.ppapb_uas.Util.SharedPreferencesHelper
import com.example.ppapb_uas.databinding.FragmentProfilesBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

// Kelas ProfilesFragment merupakan bagian dari fragment untuk menampilkan profil pengguna
class ProfilesFragment : Fragment() {

    private lateinit var binding: FragmentProfilesBinding
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    // Metode onCreateView untuk membuat tampilan fragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflasi tata letak fragment
        binding = FragmentProfilesBinding.inflate(inflater, container, false)
        val view = binding.root

        // Menginisialisasi SharedPreferencesHelper
        sharedPreferencesHelper = SharedPreferencesHelper.getInstance(requireContext())

        // Menetapkan data profil pengguna ke tampilan
        binding.frProfileTvName.text = sharedPreferencesHelper.getUserName()
        binding.frProfileTvHeight.text = sharedPreferencesHelper.getUserHeight().toString()
        binding.frProfileTvTargetweight.text = sharedPreferencesHelper.getUserTargetWeight().toString()

        // Menangani klik tombol keluar
        binding.proRlKeluar.setOnClickListener{
            // Melakukan proses logout dengan Firebase Authentication
            Firebase.auth.signOut()
            // Menavigasi kembali ke layar login
            val intent = Intent(activity, AuthActivity::class.java)
            // Membersihkan tumpukan aktivitas untuk mencegah pengguna kembali ke aktivitas ini setelah logout
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        return view
    }

    // Metode showToast untuk menampilkan pesan singkat menggunakan Toast
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
