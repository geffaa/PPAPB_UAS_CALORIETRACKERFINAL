package com.example.ppapb_uas.MenuUser

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ppapb_uas.R
import com.example.ppapb_uas.Util.SharedPreferencesHelper
import com.example.ppapb_uas.data.AdminMenuFirestore
import com.example.ppapb_uas.databinding.ActivityChooseMenuBinding
import com.google.firebase.firestore.FirebaseFirestore

// Activity untuk memilih makanan dari daftar menu admin Firestore
class ChooseMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseMenuBinding
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var admindataAdapter: ChooseMenuAdapter
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        sharedPreferencesHelper = SharedPreferencesHelper.getInstance(this@ChooseMenuActivity)

        val recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)
        admindataAdapter = ChooseMenuAdapter()
        recyclerView.adapter = admindataAdapter
        fetchDataAndObserve()

        // Navigasi ke halaman tambah makanan kustom
        binding.chooseToCustom.setOnClickListener{
            val toMainActivity = Intent(this@ChooseMenuActivity, AddFoodActivity::class.java)
            startActivity(toMainActivity)
        }

        // Set up fungsi pencarian
        val searchEditText = binding.sgnPassword
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Panggil fetchDataAndObserve dengan kueri pencarian
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

    // Metode untuk mengambil dan mengamati data dari Firestore
    private fun fetchDataAndObserve(searchQuery: String = "") {
        try {
            val makananCollection = firestore.collection("makanan")

            // Tambahkan kondisi untuk memfilter data berdasarkan kueri pencarian
            val query = if (searchQuery.isNotEmpty()) {
                makananCollection.whereGreaterThanOrEqualTo("foodName", searchQuery)
                    .whereLessThanOrEqualTo("foodName", searchQuery + "\uf8ff")
            } else {
                makananCollection
            }

            // Amati perubahan Firestore
            query.addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    showToast(this@ChooseMenuActivity, "Error fetching data from Firestore")
                    return@addSnapshotListener
                }

                snapshot?.let { documents ->
                    val makanans = mutableListOf<AdminMenuFirestore>()
                    for (document in documents) {
                        val makananId = document.id
                        val makanan = document.toObject(AdminMenuFirestore::class.java).copy(id = makananId)
                        makanans.add(makanan)
                    }

                    // Perbarui UI dengan data Firestore
                    admindataAdapter.setMakanan(makanans, searchQuery)
                }
            }
        } catch (e: Exception) {
            showToast(this@ChooseMenuActivity, e.toString())
            Log.d("ERRORKU", e.toString())
        }
    }

    // Metode untuk menampilkan pesan Toast
    private fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, duration).show()
    }
}
