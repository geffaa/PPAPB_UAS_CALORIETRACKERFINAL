package com.example.ppapb_uas.History

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.ppapb_uas.data.MenuRoomDatabase
import com.example.ppapb_uas.data.UserMenuDAO

// Kelas ini merupakan bagian dari arsitektur MVVM dan bertanggung jawab untuk mengelola data terkait riwayat menu.
class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    // Menginisialisasi objek UserMenuDAO
    private val menuUserDAO: UserMenuDAO

    // Blok inisialisasi yang dieksekusi saat kelas ini dibuat
    init {
        // Mendapatkan instance dari MenuRoomDatabase dari aplikasi
        val database = MenuRoomDatabase.getDatabase(application.applicationContext)
        // Inisialisasi menuUserDAO dengan UserMenuDAO dari database
        menuUserDAO = database?.MenuUserDAO() ?: throw Exception("Database not initialized")
    }

    // (Mungkin tambahkan logika bisnis atau fungsi lain di sini sesuai kebutuhan)
}
