package com.example.ppapb_uas.Auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.ppapb_uas.Admin.AdminActivity
import com.example.ppapb_uas.BottomNavActivity
import com.example.ppapb_uas.R
import com.example.ppapb_uas.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi Firebase Authentication
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Jika pengguna sudah login, arahkan ke BottomNavActivity
            val toUserActivity = Intent(requireContext(), BottomNavActivity::class.java)
            startActivity(toUserActivity)
        }

        sharedPreferences = requireContext().getSharedPreferences("myAppPreferences", Context.MODE_PRIVATE)

        // Set up UI components menggunakan View Binding
        binding.loginBtLogin.setOnClickListener {
            val email = binding.loginEtEmail.text.toString().trim()
            val password = binding.loginEtPassword.text.toString().trim()

            // Validasi email dan password (tambahkan logika validasi Anda sendiri)
            if (email.isEmpty() || password.isEmpty()) {
                showToast("Harap isi semua kolom.")
                return@setOnClickListener
            }

            binding.progressBarLogin.visibility = View.VISIBLE

            viewModel.loginUser(email, password) { success, message ->
                binding.progressBarLogin.visibility = View.GONE
                if (success) {
                    // Jika login berhasil, ambil peran pengguna dari Firestore
                    viewModel.fetchUserRoleFromFirestore() { role ->
                        when (role) {
                            1 -> {
                                // Tipe 1 adalah tipe pengguna
                                val toUserActivity = Intent(requireContext(), BottomNavActivity::class.java)
                                startActivity(toUserActivity)
                            }
                            2 -> {
                                // Tipe 2 adalah tipe admin
                                val toAdminActivity = Intent(requireContext(), AdminActivity::class.java)
                                startActivity(toAdminActivity)
                            }
                            else -> {
                                // Handle tipe lainnya sesuai kebutuhan
                                showToast("Tipe pengguna tidak valid")
                            }
                        }
                    }
                } else {
                    showToast("Gagal: $message")
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
