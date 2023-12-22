package com.example.ppapb_uas.MenuUser

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.ppapb_uas.BottomNavActivity
import com.example.ppapb_uas.R
import com.example.ppapb_uas.Util.SharedPreferencesHelper
import com.example.ppapb_uas.data.MenuRoomDatabase
import com.example.ppapb_uas.data.UserMenu
import com.example.ppapb_uas.data.UserMenuDAO
import com.example.ppapb_uas.databinding.ActivityAddChooseMenuBinding
import com.example.ppapb_uas.databinding.ActivityAddFoodBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

// Activity untuk menambahkan makanan yang dipilih ke dalam daftar menu pengguna
class AddChooseMenuActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: ActivityAddChooseMenuBinding
    private lateinit var mMenuUserDao: UserMenuDAO
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddChooseMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val foodName = intent.getStringExtra("foodName")
        val foodCalorie = intent.getIntExtra("foodCalorie",0)

        // Inisialisasi tampilan dengan data makanan yang dipilih
        binding.addChoosemenuName.text = Editable.Factory.getInstance().newEditable(foodName.toString())
        binding.addChoosemenuEtKalori.text = Editable.Factory.getInstance().newEditable(foodCalorie.toString())

        // Inisialisasi database
        val db = MenuRoomDatabase.getDatabase(this@AddChooseMenuActivity)
        mMenuUserDao = db?.MenuUserDAO() ?: throw Exception("Database not initialized")
        sharedPreferencesHelper = SharedPreferencesHelper.getInstance(this@AddChooseMenuActivity)

        // Spinner untuk memilih jenis makanan
        val spinner1 = binding.addChoosemenuSpinnerMakan
        val data = listOf("lunch", "dinner", "breakfast")
        var selectTypeMakan = ""

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, data)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner1.adapter = adapter

        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectTypeMakan = data[position] // Dapatkan item yang dipilih dari daftar data
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Tangani kasus ketika tidak ada yang dipilih (opsional)
            }
        }

        // Time Picker untuk memilih waktu makan
        binding.addChoosemenuTime.setOnClickListener {
            val timepicker = TimePicker()
            timepicker.show(supportFragmentManager, "TP")
        }

        // Tombol untuk menambahkan makanan ke dalam daftar menu pengguna
        binding.addChoosemenuBtSubmit.setOnClickListener {
            var nameMakan = binding.addChoosemenuName.text.toString()
            var waktuMakan = binding.addChoosemenuTime.text.toString()
            var jumlahKaloriNew = binding.addChoosemenuEtKalori.text.toString()

            try {
                // Coba konversi jumlahKaloriNew ke integer
                val kaloriInt = jumlahKaloriNew.toInt()

                if (nameMakan.isBlank() || waktuMakan.isBlank() || kaloriInt <= 0 || selectTypeMakan.isBlank()) {
                    Toast.makeText(this, "Data tidak valid!", Toast.LENGTH_SHORT).show()
                } else {
                    // Gunakan coroutines untuk melakukan penambahan data ke database di latar belakang
                    CoroutineScope(Dispatchers.IO).launch {
                        val menuUser = UserMenu(
                            userId = sharedPreferencesHelper.getUserId().toString(),
                            type = selectTypeMakan,
                            action = "makan",
                            foodName = nameMakan,
                            foodCalorie = kaloriInt,
                            date = getCurrentDateInGMTPlus7()
                        )

                        mMenuUserDao.insert(menuUser)
                        val toMainActivity = Intent(this@AddChooseMenuActivity, BottomNavActivity::class.java)
                        startActivity(toMainActivity)
                    }
                }
            } catch (e: NumberFormatException) {
                // Tangani kasus ketika konversi ke Int gagal
                Toast.makeText(this, "Input numerik tidak valid!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Metode untuk menangani kejadian setelah pemilihan waktu pada TimePicker
    override fun onTimeSet(view: android.widget.TimePicker?, hourOfDay: Int, minute: Int) {
        val selectedTime =  String.format("%02d:%02d", hourOfDay, minute)
        binding.addChoosemenuTime.setText(selectedTime)
    }

    // Metode untuk mendapatkan tanggal saat ini di zona waktu GMT+7
    fun getCurrentDateInGMTPlus7(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        dateFormat.timeZone = TimeZone.getTimeZone("GMT+7")
        return dateFormat.format(Date())
    }
}
