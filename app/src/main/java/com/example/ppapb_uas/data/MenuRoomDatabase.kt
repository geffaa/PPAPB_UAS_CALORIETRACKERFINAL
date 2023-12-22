package com.example.ppapb_uas.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserMenu::class, AdminMenu::class],
    version = 2, // Ubah versi ke versi yang lebih tinggi
    exportSchema = false)

// Database Room untuk menyimpan data terkait pengguna dan admin
abstract class MenuRoomDatabase : RoomDatabase() {

    abstract fun MenuUserDAO(): UserMenuDAO?      // Akses objek akses data (DAO) untuk entitas pengguna
    abstract fun MenuAdminDAO(): AdminMenuDAO?    // Akses objek DAO untuk entitas admin

    companion object {
        @Volatile
        private var INSTANCE: MenuRoomDatabase? = null

        // Fungsi untuk mendapatkan instance tunggal dari database
        fun getDatabase(context: Context): MenuRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(MenuRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MenuRoomDatabase::class.java, "menu_db"
                    )
                        .fallbackToDestructiveMigration() // Menambahkan fallbackToDestructiveMigration jika perlu
                        .build()
                }
            }
            return INSTANCE
        }
    }
}
