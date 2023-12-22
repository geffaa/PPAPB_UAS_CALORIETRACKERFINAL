package com.example.ppapb_uas.Auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.ppapb_uas.GetStarted.GetStartedActivity
import com.example.ppapb_uas.R
import com.example.ppapb_uas.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi Firebase Authentication dan Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Set up UI components menggunakan View Binding
        binding.signupBtnSignup.setOnClickListener {
            val email = binding.signupEtEmail.editText?.text.toString().trim()
            val password = binding.signupEtPassword.editText?.text.toString().trim()

            // Validasi email dan password (tambahkan logika validasi Anda sendiri)
            if (email.isEmpty() || password.isEmpty()) {
                // Handle kolom yang kosong
                showToast("Harap isi semua kolom.")
                return@setOnClickListener
            }

            binding.progressBarSignUp.visibility = View.VISIBLE

            // Buat pengguna dengan email dan password
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    try {
                        binding.progressBarSignUp.visibility = View.GONE
                        if (task.isSuccessful) {
                            // Pendaftaran berhasil
                            val user = auth.currentUser
                            // Tambahkan data tambahan ke Firestore
                            addUserToFirestore(user?.uid, email)
                            val welcomeIntent = Intent(requireContext(), GetStartedActivity::class.java)
                            welcomeIntent.putExtra("val_uid", user?.uid);
                            welcomeIntent.putExtra("val_email", email);
                            startActivity(welcomeIntent)

                            showToast("Pendaftaran Berhasil")
                        } else {
                            // Jika pendaftaran gagal, tampilkan pesan ke pengguna.
                            throw task.exception ?: Exception("Terjadi kesalahan yang tidak diketahui.")
                        }
                    } catch (e: Exception) {
                        showToast("Pendaftaran gagal: ${e.message}")
                    }
                }
        }
    }

    private fun addUserToFirestore(userId: String?, email: String) {
        // Tambahkan data pengguna ke Firestore
        val user = hashMapOf(
            "email" to email,
            // Tambahkan data tambahan yang ingin Anda simpan
        )

        if (userId != null) {
            firestore.collection("users")
                .document(userId)
                .set(user)
                .addOnSuccessListener {
                    // Handle keberhasilan
                }
                .addOnFailureListener { e ->
                    // Handle kegagalan
                }
        }
    }

    companion object {
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
