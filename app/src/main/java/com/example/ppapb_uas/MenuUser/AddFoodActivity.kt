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
import com.example.ppapb_uas.databinding.ActivityAddFoodBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

// Activity untuk menambahkan makanan ke dalam daftar menu pengguna
class AddFoodActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: ActivityAddFoodBinding
    private lateinit var mMenuUserDao: UserMenuDAO
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFoodBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Inisialisasi database
        val db = MenuRoomDatabase.getDatabase(this@AddFoodActivity)
        mMenuUserDao = db?.MenuUserDAO() ?: throw Exception("Database not initialized")
        sharedPreferencesHelper = SharedPreferencesHelper.getInstance(this@AddFoodActivity)

        // Spinner untuk memilih jenis makanan
        val spinner1 = binding.makanSpinnerMakan
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
        binding.makanTime.setOnClickListener {
            val timepicker = TimePicker()
            timepicker.show(supportFragmentManager, "TP")
        }

        // Tombol untuk menambahkan makanan ke dalam daftar menu pengguna
        binding.makanBtSubmit.setOnClickListener {
            var nameMakan = binding.makanEtName.text.toString()
            var waktuMakan = binding.makanTime.text.toString()
            var jumlahKaloriNew = binding.makanEtKalori.text.toString()

            try {
                // Coba konversi jumlahKaloriNew dan jumlahServing ke dalam integer
                val kaloriInt = jumlahKaloriNew.toInt()

                if (nameMakan.isBlank() || waktuMakan.isBlank() || kaloriInt <= 0 ||  selectTypeMakan.isBlank()) {
                    Toast.makeText(this, "Data tidak valid!", Toast.LENGTH_SHORT).show()
                } else {
                    // Gunakan coroutines untuk melakukan penambahan data ke dalam database di latar belakang
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
                        val toMainActivity = Intent(this@AddFoodActivity, BottomNavActivity::class.java)
                        startActivity(toMainActivity)

                        createNotification()

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
        binding.makanTime.setText(selectedTime)
    }

    // Metode untuk mendapatkan tanggal saat ini di zona waktu GMT+7
    fun getCurrentDateInGMTPlus7(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        dateFormat.timeZone = TimeZone.getTimeZone("GMT+7")
        return dateFormat.format(Date())
    }

    // Metode untuk membuat notifikasi
    private fun createNotification() {
        // Notification channel diperlukan untuk Android Oreo (API level 26) dan yang lebih tinggi
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "YOUR_CHANNEL_ID",
                "YOUR_CHANNEL_NAME",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Bangun notifikasi
        val builder = NotificationCompat.Builder(this, "YOUR_CHANNEL_ID")
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle("CalorieCare")
            .setContentText("Berhasil menambahkan makanan")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Tampilkan notifikasi
        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@AddFoodActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(1, builder.build())
        }
    }

}