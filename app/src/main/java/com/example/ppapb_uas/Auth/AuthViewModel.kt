package com.example.ppapb_uas.Auth

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.ppapb_uas.Util.SharedPreferencesHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    // Inisialisasi objek Firebase Authentication
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Inisialisasi objek SharedPreferencesHelper
    private val sharedPreferencesHelper =
        SharedPreferencesHelper.getInstance(application.applicationContext)

    // Inisialisasi objek Firebase Firestore
    private var firestore = FirebaseFirestore.getInstance()

    // LiveData untuk menyimpan data peran pengguna (user role)
    private val _userRole = MutableLiveData<Int?>()

    val userRole: MutableLiveData<Int?>
        get() = _userRole

    // Fungsi untuk mengambil peran pengguna dari Firestore
    fun fetchUserRoleFromFirestore(onResult: (Int) -> Unit) {
        val userId = auth.currentUser?.uid

        userId?.let { uid ->
            firestore.collection("users")
                .whereEqualTo("userId", uid)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val tipe = document.getLong("tipe")

                        // Menampilkan peran yang diambil dengan menggunakan pesan toast
                        if (tipe != null) {
                            onResult(tipe.toInt())
                        }
                    }
                }
                .addOnFailureListener {
                    // Menghandle kegagalan
                    onResult(-1) // Misalnya, -1 untuk menunjukkan kegagalan atau tipe tidak valid
                }
        }
    }

    // Fungsi untuk menyimpan data pengguna dari Firestore ke SharedPreferences
    fun saveDataFromFirestore(){
        val userId = auth.currentUser?.uid

        userId?.let { uid ->
            firestore.collection("users")
                .whereEqualTo("userId", uid)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val tipe = document.getLong("tipe")

                        // Menyimpan data pengguna ke SharedPreferences
                        sharedPreferencesHelper.saveUserName(document.getString("name").toString())
                        sharedPreferencesHelper.saveUserId(document.getString("userId").toString())
                        document.getLong("tipe")
                            ?.let { sharedPreferencesHelper.saveUserTipe(it.toInt()) }

                        document.getLong("height")
                            ?.let { sharedPreferencesHelper.saveUserHeight(it.toInt()) }

                        document.getLong("weight")
                            ?.let { sharedPreferencesHelper.saveUserWeight(it.toInt()) }

                        document.getLong("target_weight")
                            ?.let { sharedPreferencesHelper.saveUserTargetWeight(it.toInt()) }

                        document.getLong("calorie")
                            ?.let { sharedPreferencesHelper.saveUserCalorie(it.toInt()) }
                    }
                }
                .addOnFailureListener {
                    // Menghandle kegagalan
                }
        }
    }

    // Fungsi untuk melakukan login pengguna
    fun loginUser(email: String, password: String, onResult: (Boolean, String) -> Unit) {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Jika login berhasil, simpan data pengguna dari Firestore ke SharedPreferences
                        saveDataFromFirestore()
                        onResult(true, "Login berhasil")
                        sharedPreferencesHelper.setLoggedIn(true)
                        sharedPreferencesHelper.saveUserId(getUserId())
                    } else {
                        onResult(false, "Login gagal: ${task.exception?.message ?: "Terjadi kesalahan"}")
                    }
                }
        } catch (e: Exception) {
            onResult(false, "Terjadi kesalahan: ${e.message ?: "Unknown error"}")
        }
    }

    // Fungsi untuk mendapatkan ID pengguna
    fun getUserId(): String {
        return auth.currentUser?.uid ?: ""
    }

    // Fungsi untuk menampilkan pesan toast
    private fun showToast(message: String) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show()
    }
}
