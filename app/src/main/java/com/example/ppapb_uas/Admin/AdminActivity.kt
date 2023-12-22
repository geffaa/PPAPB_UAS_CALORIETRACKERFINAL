package com.example.ppapb_uas.Admin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ppapb_uas.Auth.AuthActivity
import com.example.ppapb_uas.R
import com.example.ppapb_uas.Util.SharedPreferencesHelper
import com.example.ppapb_uas.data.AdminMenuFirestore
import com.example.ppapb_uas.databinding.ActivityAdminBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var admindataAdapter: AdminAdapter
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menggunakan view binding untuk mengakses elemen UI
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi Firebase Firestore untuk mengakses database
        firestore = FirebaseFirestore.getInstance()

        // Inisialisasi helper untuk berbagi data secara lokal
        sharedPreferencesHelper = SharedPreferencesHelper.getInstance(this@AdminActivity)

        // Konfigurasi RecyclerView untuk menampilkan daftar makanan admin
        val recyclerView = binding.recyclerviewAdmin
        recyclerView.layoutManager = LinearLayoutManager(this)
        admindataAdapter = AdminAdapter()
        recyclerView.adapter = admindataAdapter

        // Mengambil dan mengamati data dari Firestore
        fetchDataAndObserve()

        // Tombol Logout
        binding.adminLogout.setOnClickListener {
            // Kembali ke layar login dan set status login menjadi false
            val toMainActivity = Intent(this@AdminActivity, AuthActivity::class.java)
            sharedPreferencesHelper.setLoggedIn(false)
            startActivity(toMainActivity)
        }

        // Tombol untuk menambahkan makanan baru
        binding.adminFabAdd.setOnClickListener{
            // Buka layar untuk menambahkan makanan baru
            val toMainActivity = Intent(this@AdminActivity, AdminAddFoodActivity::class.java)
            startActivity(toMainActivity)
        }

        // Konfigurasi pencarian makanan
        val searchEditText = binding.sgnPassword
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Memanggil fetchDataAndObserve dengan query pencarian
                fetchDataAndObserve(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Tidak diperlukan
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Tidak diperlukan
            }
        })
    }

    // Mengambil data dari Firestore dan mengamati perubahan
    private fun fetchDataAndObserve(searchQuery: String = "") {
        try {
            val bukuCollection = firestore.collection("makanan")

            // Menambahkan kondisi untuk memfilter data berdasarkan query pencarian
            val query = if (searchQuery.isNotEmpty()) {
                bukuCollection.whereGreaterThanOrEqualTo("foodName", searchQuery)
                    .whereLessThanOrEqualTo("foodName", searchQuery + "\uf8ff")
            } else {
                bukuCollection
            }

            // Mengamati perubahan Firestore
            query.addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    showToast(this@AdminActivity, "Error fetching data from Firestore")
                    return@addSnapshotListener
                }

                snapshot?.let { documents ->
                    val bukus = mutableListOf<AdminMenuFirestore>()
                    for (document in documents) {
                        // Mendapatkan data dan mengonversinya ke objek AdminMenuFirestore
                        val bukuId = document.id
                        val buku = document.toObject(AdminMenuFirestore::class.java).copy(id = bukuId)
                        bukus.add(buku)
                    }

                    // Memperbarui UI dengan data Firestore
                    admindataAdapter.setMakanan(bukus, searchQuery)
                }
            }
        } catch (e: Exception) {
            showToast(this@AdminActivity, e.toString())
            Log.d("ERRORKU", e.toString())
        }
    }

    // Metode utilitas untuk menampilkan pesan toast
    private fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, duration).show()
    }
}
