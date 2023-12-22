package com.example.ppapb_uas.Admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import com.example.ppapb_uas.R
import com.example.ppapb_uas.databinding.ActivityAdminEditFoodBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminEditFoodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminEditFoodBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val makananCollection = firestore.collection("makanan")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminEditFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mendapatkan data dari intent
        val id = intent.getStringExtra("id")
        val foodName = intent.getStringExtra("foodName")
        val foodCalorie = intent.getIntExtra("foodCalorie", 0)

        // Mengatur nilai awal pada EditText berdasarkan data yang diterima
        binding.adminEditEtFoodName.text = Editable.Factory.getInstance().newEditable(foodName.toString())
        binding.adminEditFoodCalorie.text = Editable.Factory.getInstance().newEditable(foodCalorie.toString())

        with(binding) {
            adminEditBtSubmit.setOnClickListener {
                // Menampilkan ProgressBar saat data sedang diproses
                adminEditProgressBar.setVisibility(View.VISIBLE)
                // Mendapatkan input dari pengguna
                var nameMakan = adminEditEtFoodName.text.toString()

                try {
                    // Mengonversi input kalori menjadi tipe data Int
                    val kaloriInt = adminEditFoodCalorie.text.toString().toInt()

                    // Memeriksa validitas data
                    if (nameMakan.isBlank() || kaloriInt <= 0) {
                        Toast.makeText(this@AdminEditFoodActivity, "Data tidak valid!", Toast.LENGTH_SHORT).show()
                    } else {
                        // Memeriksa apakah ID telah diterima
                        id?.let {
                            // Memperbarui data di Firestore
                            makananCollection.document(it)
                                .update(
                                    mapOf(
                                        "foodName" to nameMakan,
                                        "foodCalorie" to kaloriInt
                                    )
                                )
                                .addOnSuccessListener {
                                    // Menyembunyikan ProgressBar setelah proses selesai
                                    adminEditProgressBar.setVisibility(View.GONE)
                                    // Menampilkan pesan berhasil jika proses berhasil
                                    showToast("Data Updated successfully")
                                    // Kembali ke layar utama AdminActivity
                                    val intent = Intent(this@AdminEditFoodActivity, AdminActivity::class.java)
                                    startActivity(intent)
                                }
                                .addOnFailureListener { e ->
                                    // Menyembunyikan ProgressBar setelah proses selesai
                                    adminEditProgressBar.setVisibility(View.GONE)
                                    // Menampilkan pesan error jika terjadi kesalahan
                                    showToast("Failed to update data: $e")
                                }
                        }
                    }
                } catch (e: NumberFormatException) {
                    // Menangani kasus jika konversi ke Int gagal
                    Toast.makeText(this@AdminEditFoodActivity, "Invalid numeric input!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Fungsi untuk menampilkan pesan singkat (Toast)
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
