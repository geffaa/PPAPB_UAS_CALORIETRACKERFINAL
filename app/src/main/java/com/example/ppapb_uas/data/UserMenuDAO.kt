package com.example.ppapb_uas.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
// Interface DAO (Data Access Object) untuk entitas UserMenu yang berinteraksi dengan database Room
interface UserMenuDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    // Fungsi untuk menambahkan data menu ke dalam tabel usermenu_table
    fun insert(menu: UserMenu)

    @Update
    // Fungsi untuk mengupdate data menu di dalam tabel usermenu_table
    fun update(menu: UserMenu)

    @Delete
    // Fungsi untuk menghapus data menu dari tabel usermenu_table
    fun delete(menu: UserMenu)

    @get:Query("SELECT * from usermenu_table ORDER BY id DESC")
    // Query untuk mendapatkan semua data menu dari tabel usermenu_table secara descending order berdasarkan ID
    val allMenus: LiveData<List<UserMenu>>

    @Query("SELECT * FROM usermenu_table WHERE userId = :userId AND date = :date ORDER BY id DESC")
    // Query untuk mendapatkan data menu berdasarkan ID pengguna (userId) dan tanggal (date) secara descending order berdasarkan ID
    fun allMenusByUserId(userId: String, date: String): LiveData<List<UserMenu>>

    @Query("SELECT * FROM usermenu_table WHERE userId = :userId AND type = :category AND date = :date ORDER BY id DESC")
    // Query untuk mendapatkan data menu berdasarkan ID pengguna (userId), kategori (category), dan tanggal (date) secara descending order berdasarkan ID
    fun allMenusByCategory(userId: String, category: String, date: String): LiveData<List<UserMenu>>

    @Query("SELECT SUM(foodCalorie) FROM usermenu_table WHERE userId = :userId AND date = :date")
    // Query untuk menghitung total kalori berdasarkan ID pengguna (userId) dan tanggal (date)
    fun getTotalCaloriesByUserIdAndDate(userId: String, date: String): LiveData<Int>

    @Query("SELECT SUM(foodCalorie) FROM usermenu_table WHERE userId = :userId AND date = :date AND type = :type")
    // Query untuk menghitung total kalori berdasarkan ID pengguna (userId), tanggal (date), dan kategori (type)
    fun getCaloriesByType(userId: String, date: String, type: String): LiveData<Int>
}
