package com.example.ppapb_uas.Admin

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.ppapb_uas.R
import com.example.ppapb_uas.Util.SharedPreferencesHelper
import com.example.ppapb_uas.data.AdminMenuFirestore
import com.example.ppapb_uas.databinding.ActivityAdminAddFoodBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class AdminAddFoodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminAddFoodBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAddFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        sharedPreferencesHelper = SharedPreferencesHelper.getInstance(this@AdminAddFoodActivity)

        with(binding) {
            adminAddBtSubmit.setOnClickListener {

                // Mendapatkan input dari pengguna
                var nameMakan = adminAddEtFoodName.text.toString()

                try {
                    // Mengonversi input kalori menjadi tipe data Int
                    val kaloriInt = adminAddEtFoodCalorie.text.toString().toInt()

                    // Memeriksa validitas data
                    if (nameMakan.isBlank() || kaloriInt <= 0) {
                        Toast.makeText(
                            this@AdminAddFoodActivity,
                            "Data tidak valid!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // Menampilkan ProgressBar saat data sedang diproses
                        adminAddProgressBar.setVisibility(View.VISIBLE)
                        // Memanggil fungsi untuk menyimpan data ke Firestore
                        insertToFireStore(nameMakan, kaloriInt)
                    }
                } catch (e: NumberFormatException) {
                    // Menangani kasus jika konversi ke Int gagal
                    Toast.makeText(
                        this@AdminAddFoodActivity,
                        "Invalid numeric input!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    // Fungsi untuk menyimpan data makanan ke Firestore
    private fun insertToFireStore(nameMakan: String, kaloriInt: Int) {
        val db = FirebaseFirestore.getInstance()

        // Membuat objek data makanan
        val menuAdminFS = AdminMenuFirestore(
            foodName = nameMakan,
            foodCalorie = kaloriInt,
            date = getCurrentDateInGMTPlus7(),
            userId = sharedPreferencesHelper.getUserId().toString()
        )

        // Menambahkan dokumen baru ke Firestore
        db.collection("makanan")
            .add(menuAdminFS)
            .addOnSuccessListener { documentReference ->
                // Mendapatkan ID dokumen yang baru dibuat
                val generatedId = documentReference.id

                // Menyembunyikan ProgressBar setelah proses selesai
                binding.adminAddProgressBar.setVisibility(View.GONE)

                // Mengupdate dokumen di Firestore dengan ID yang baru
                db.collection("makanan")
                    .document(generatedId)
                    .set(menuAdminFS.copy(id = generatedId))
                    .addOnSuccessListener {
                        // Menampilkan pesan berhasil jika proses berhasil
                        Toast.makeText(
                            this@AdminAddFoodActivity,
                            "Data berhasil ditambahkan",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Membuat notifikasi setelah menambahkan data makanan
                        createNotification()

                        // Kembali ke layar utama AdminActivity
                        val toMainActivity = Intent(
                            this@AdminAddFoodActivity,
                            AdminActivity::class.java
                        )
                        startActivity(toMainActivity)
                    }
                    .addOnFailureListener { e ->
                        // Menampilkan pesan error jika terjadi kesalahan
                        Toast.makeText(
                            this@AdminAddFoodActivity,
                            "Error updating document with ID $generatedId: $e",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
            .addOnFailureListener { e ->
                // Menampilkan pesan error jika terjadi kesalahan
                Toast.makeText(
                    this@AdminAddFoodActivity,
                    "Error adding data to Firestore: $e",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    // Fungsi untuk mendapatkan tanggal hari ini dalam zona waktu GMT+7
    fun getCurrentDateInGMTPlus7(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        dateFormat.timeZone = TimeZone.getTimeZone("GMT+7")
        return dateFormat.format(Date())
    }

    // Fungsi untuk membuat notifikasi
    private fun createNotification() {
        // Notification channel diperlukan untuk Android Oreo (API level 26) dan lebih tinggi
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

        // Membangun notifikasi
        val builder = NotificationCompat.Builder(this, "YOUR_CHANNEL_ID")
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle("CalorieCare")
            .setContentText("Admin Baru Saja Menambahkan Makanan")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Menampilkan notifikasi
        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@AdminAddFoodActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(1, builder.build())
        }
    }
}
