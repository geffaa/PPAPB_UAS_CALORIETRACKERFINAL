package com.example.ppapb_uas.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

// Data Access Object (DAO) untuk AdminMenu
@Dao
interface AdminMenuDAO {

    // Insert data ke tabel adminmenu_table, jika terjadi konflik, abaikan data baru
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(menu: AdminMenu)

    // Mendapatkan data yang belum disinkronkan (contoh: data yang belum diunggah ke server)
    @Query("SELECT * from adminmenu_table")
    fun getUnsyncedData(): LiveData<List<AdminMenu>>

    // Menghapus semua data dari tabel adminmenu_table
    @Query("DELETE FROM adminmenu_table")
    fun deleteAllMenuAdmin()

    // Menghapus data adminmenu dari tabel adminmenu_table
    @Delete
    fun delete(menu: AdminMenu)
}
