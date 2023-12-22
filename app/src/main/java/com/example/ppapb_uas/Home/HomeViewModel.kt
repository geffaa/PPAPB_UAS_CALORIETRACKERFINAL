package com.example.ppapb_uas.Home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ppapb_uas.data.MenuRoomDatabase
import com.example.ppapb_uas.data.UserMenuDAO

// Kelas ViewModel untuk mengelola logika bisnis terkait tampilan beranda
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    // Objek DAO untuk mengakses data dari database
    private val menuUserDAO: UserMenuDAO

    init {
        // Inisialisasi objek database
        val database = MenuRoomDatabase.getDatabase(application.applicationContext)
        // Inisialisasi objek DAO
        menuUserDAO = database?.MenuUserDAO() ?: throw Exception("Database not initialized")
    }

    // LiveData untuk teks pada tampilan beranda
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    // Mendapatkan total kalori untuk pengguna dan tanggal tertentu
    fun getTotalCaloriesForCurrentUserAndDate(userId: String, date: String): LiveData<Int> {
        return menuUserDAO.getTotalCaloriesByUserIdAndDate(userId, date)
    }

    // Mendapatkan jumlah kalori berdasarkan jenis (breakfast, lunch, dinner) untuk pengguna dan tanggal tertentu
    fun getCaloriesByType(userId: String, date: String, type: String): LiveData<Int> {
        return menuUserDAO.getCaloriesByType(userId, date, type)
    }
}
