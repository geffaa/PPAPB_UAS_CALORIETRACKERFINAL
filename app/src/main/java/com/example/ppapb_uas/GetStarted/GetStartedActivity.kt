package com.example.ppapb_uas.GetStarted

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.ppapb_uas.BottomNavActivity
import com.example.ppapb_uas.R
import com.example.ppapb_uas.Util.SharedPreferencesHelper
import com.example.ppapb_uas.databinding.ActivityGetStartedBinding
import com.google.firebase.firestore.FirebaseFirestore

class GetStartedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGetStartedBinding
    private var selectedDate: String = ""
    private lateinit var firestore: FirebaseFirestore
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetStartedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        sharedPreferencesHelper = SharedPreferencesHelper.getInstance(this@GetStartedActivity)

        // Array untuk Spinner
        val satuanBerat = arrayOf("Kg", "Lb")
        val tujuanDiet = arrayOf("Pilih tujuan Diet anda", "bulking", "cutting", "maintaining")
        val satuanKalori = arrayOf("kal", "kkal")

        val intent = intent
        var val_name = intent.getStringExtra("val_name")
        var val_uid = intent.getStringExtra("val_uid")
        var val_email = intent.getStringExtra("val_email")

        // OnClickListener untuk DatePicker
        binding.gsdatePickerTextInputEditText.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this)
            datePickerDialog.setOnDateSetListener { _, year, month, dayOfMonth ->
                selectedDate = "$dayOfMonth/$month/$year"
                binding.gsdatePickerTextInputEditText.setText(selectedDate)
            }
            datePickerDialog.show()
        }

        // ArrayAdapter untuk Spinner
        binding.spinnerMaksimumKalori.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, satuanKalori)
        binding.spinnerSatuanBerat1.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, satuanBerat)
        binding.spinnerSatuanBerat2.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, satuanBerat)

        val adapter = object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tujuanDiet) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val tv = view as TextView
                if (position == 0) {
                    tv.setTextColor(Color.GRAY)
                } else {
                    tv.setTextColor(Color.BLACK)
                }
                return view
            }
        }

        binding.spinnerTujuanDiet.adapter = adapter

        // OnClickListener untuk tombol submit
        binding.btnSubmit.setOnClickListener {

            val spinnerTujuanDiet = findViewById<Spinner>(R.id.spinnerTujuanDiet)
            val tujuanDiet = spinnerTujuanDiet.selectedItem.toString()

            // Simpan tujuan diet yang dipilih ke SharedPreferences
            sharedPreferencesHelper.saveUserTujuanDiet(tujuanDiet)

            // Mendapatkan nilai dari input pengguna
            val beratsaatini = binding.etBbsekarang.text.toString()
            val beratyangdiinginkan = binding.etBbtarget.text.toString()
            val jumlahkalori = binding.edtMaksimumkalori.text.toString()
            val targetTanggal = binding.gsdatePickerTextInputEditText.text.toString()
            val tinggiBadan = binding.etTinggibadan.text.toString()
            val nama = binding.etName.text.toString()
            val tujuandiet = binding.spinnerTujuanDiet.selectedItem.toString()
            val selectedSatuanBerat1 = binding.spinnerSatuanBerat1.selectedItem.toString()
            val selectedSatuanBerat2 = binding.spinnerSatuanBerat2.selectedItem.toString()

            // Validasi input pengguna
            if (beratsaatini.isFloat() && beratyangdiinginkan.isFloat() && jumlahkalori.isFloat() && tinggiBadan.isFloat()) {
                if (targetTanggal == ""){
                    Toast.makeText(this, "Data tidak cocok!", Toast.LENGTH_SHORT).show()
                } else {

                    Log.d("data_name", val_name.toString())
                    Log.d("data_uid", val_uid.toString())
                    Log.d("data_beratsaatini",beratsaatini)
                    Log.d("data_satuanberatsaatini", satuanBerat.toString())
                    Log.d("data_beratyangdiinginkan",beratyangdiinginkan)
                    Log.d("data_satuanberatyangdiinginkan",satuanBerat.toString())
                    Log.d("data_targetTanggal",targetTanggal)
                    Log.d("data_jumlahkalori",jumlahkalori)
                    // Menambahkan data pengguna ke Firestore
                    addUserDataToFirestore(val_uid, nama, beratsaatini.toInt(), selectedSatuanBerat1, beratyangdiinginkan.toInt(), selectedSatuanBerat2, targetTanggal, jumlahkalori.toInt(), val_email.toString(), 1, tinggiBadan.toInt(), tujuandiet)
                }
            } else {
                Toast.makeText(this, "Data tidak cocok!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Fungsi untuk menambahkan data pengguna ke Firestore
    private fun addUserDataToFirestore(userId: String?, val_name: String, weight: Int, weight_unit: String, target_weight: Int, target_weight_unit: String, target_date: String, calorie: Int, email: String, tipe: Int, tinggiBadan: Int, tujuandiet: String) {
        val user = hashMapOf(
            "userId" to userId,
            "name" to val_name,
            "weight" to weight,
            "weight_unit" to weight_unit,
            "target_weight" to target_weight,
            "target_weight_unit" to target_weight_unit,
            "target_date" to target_date,
            "calorie" to calorie,
            "tujuandiet" to tujuandiet,
            "email" to email,
            "tipe" to tipe,
            "height" to tinggiBadan
        )

        if (userId != null) {
            try {
                firestore.collection("users")
                    .document(userId)
                    .set(user)
                    .addOnSuccessListener {
                        showToast("Data Berhasil Ditambahkan")

                        // Menyimpan data ke SharedPreferences
                        sharedPreferencesHelper.saveUserWeight(weight)
                        sharedPreferencesHelper.saveUserTargetWeight(target_weight)
                        sharedPreferencesHelper.saveUserTujuanDiet(tujuandiet)
                        sharedPreferencesHelper.saveUserCalorie(calorie)
                        sharedPreferencesHelper.saveUserTargetDate(target_date)
                        sharedPreferencesHelper.saveUserName(val_name)
                        sharedPreferencesHelper.saveUserId(userId)
                        sharedPreferencesHelper.saveUserTipe(tipe)
                        sharedPreferencesHelper.saveUserHeight(tinggiBadan)
                        sharedPreferencesHelper.setLoggedIn(true)

                        // Pindah ke activity utama setelah data berhasil disimpan
                        val toMainActivity = Intent(this@GetStartedActivity, BottomNavActivity::class.java)
                        startActivity(toMainActivity)
                    }

                    .addOnFailureListener { e ->
                        showToast("Data Gagal Ditambahkan")
                        Log.d("KESALAHAN", e.message.toString())
                    }
            } catch (e: Exception) {
                showToast("Terjadi kesalahan: ${e.message}")
                Log.d("KESALAHAN", e.message.toString())
            }
        }
    }

    // Fungsi untuk mengecek apakah String dapat diubah menjadi float
    fun String.isFloat(): Boolean {
        return try {
            this.toFloat()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    // Fungsi untuk menampilkan pesan Toast
    fun showToast(message: String) {
        Toast.makeText(this@GetStartedActivity, message, Toast.LENGTH_SHORT).show()
    }
}
