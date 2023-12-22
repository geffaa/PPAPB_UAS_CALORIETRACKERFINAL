package com.example.ppapb_uas.Util

import android.content.Context
import android.content.SharedPreferences

// Kelas untuk mengelola penyimpanan preferensi bersama (SharedPreferences) terkait autentikasi dan informasi pengguna.
class SharedPreferencesHelper(private val context: Context) {

    // SharedPreferences yang digunakan untuk menyimpan data.
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "authAppPrefs"

        // Kunci-kunci untuk menyimpan dan mengambil data dari SharedPreferences.
        private const val KEY_USER_ID = "userIdFromFirebase"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_NAME = "NAME"
        private const val KEY_USERID = "USERID"
        private const val KEY_ROLE = "ROLE"
        private const val KEY_TIPE = "TIPE"
        private const val KEY_HEIGHT = "HEIGHT"
        private const val  KEY_WEIGHT = "WEIGHT"
        private const val KEY_TARGETWEIGHT = "TARGETWEIGHT"
        private const val KEY_CALORIE = "CALORIE"
        private const val KEY_TARGET_DATE = "TARGET_DATE"
        private const val KEY_TUJUAN_DIET = "TUJUAN_DIET"

        // Variabel untuk mengimplementasikan pola desain Singleton.
        @Volatile
        private var instance: SharedPreferencesHelper? = null

        fun getInstance(context: Context): SharedPreferencesHelper {
            return instance ?: synchronized(this) {
                instance ?: SharedPreferencesHelper(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }

    // Fungsi untuk menyimpan status login pengguna.
    fun setLoggedIn(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    // Fungsi untuk memeriksa apakah pengguna sudah login atau tidak.
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    // Fungsi untuk menyimpan ID pengguna.
    fun saveUserId(userId: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USERID, userId)
        editor.apply()
    }

    // Fungsi untuk mendapatkan ID pengguna yang disimpan.
    fun getUserId(): String? {
        return sharedPreferences.getString(KEY_USERID, null)
    }

    // Fungsi untuk menyimpan peran (role) pengguna.
    fun saveUserRole(userId: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_ROLE, userId)
        editor.apply()
    }

    // Fungsi untuk mendapatkan peran (role) pengguna yang disimpan.
    fun getUserRole(): String? {
        return sharedPreferences.getString(KEY_ROLE, null)
    }

    // Fungsi untuk menyimpan tipe pengguna.
    fun saveUserTipe(tipe: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_TIPE, tipe)
        editor.apply()
    }

    // Fungsi untuk mendapatkan tipe pengguna yang disimpan.
    fun getUserTipe(): Int {
        return sharedPreferences.getInt(KEY_TIPE, 0)
    }

    // Fungsi untuk menyimpan nama pengguna.
    fun saveUserName(name: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_NAME, name)
        editor.apply()
    }

    // Fungsi untuk mendapatkan nama pengguna yang disimpan.
    fun getUserName(): String? {
        return sharedPreferences.getString(KEY_NAME, null)
    }

    // Fungsi untuk menyimpan tinggi badan pengguna.
    fun saveUserHeight(height: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_HEIGHT, height)
        editor.apply()
    }

    // Fungsi untuk mendapatkan tinggi badan pengguna yang disimpan.
    fun getUserHeight(): Int {
        return sharedPreferences.getInt(KEY_HEIGHT, 0)
    }

    // Fungsi untuk menyimpan berat badan pengguna.
    fun saveUserWeight(weight: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_WEIGHT, weight)
        editor.apply()
    }

    // Fungsi untuk mendapatkan berat badan pengguna yang disimpan.
    fun getUserWeight(): Int {
        return sharedPreferences.getInt(KEY_WEIGHT, 0)
    }

    // Fungsi untuk menyimpan target berat badan pengguna.
    fun saveUserTargetWeight(targetWeight: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_TARGETWEIGHT, targetWeight)
        editor.apply()
    }

    // Fungsi untuk mendapatkan target berat badan pengguna yang disimpan.
    fun getUserTargetWeight(): Int {
        return sharedPreferences.getInt(KEY_TARGETWEIGHT, 0)
    }

    // Fungsi untuk menyimpan tujuan diet pengguna.
    fun saveUserTujuanDiet(tujuanDiet: String) {
        sharedPreferences.edit().putString(KEY_TUJUAN_DIET, tujuanDiet).apply()
    }

    // Fungsi untuk mendapatkan tujuan diet pengguna yang disimpan.
    fun getUserTujuanDiet(): String {
        return sharedPreferences.getString(KEY_TUJUAN_DIET, "") ?: ""
    }

    // Fungsi untuk menyimpan jumlah kalori harian pengguna.
    fun saveUserCalorie(calorie: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_CALORIE, calorie)
        editor.apply()
    }

    // Fungsi untuk mendapatkan jumlah kalori harian pengguna yang disimpan.
    fun getUserCalorie(): Int {
        return sharedPreferences.getInt(KEY_CALORIE, 0)
    }

    // Fungsi untuk membersihkan semua data yang disimpan di SharedPreferences.
    fun clear() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    // Fungsi untuk menyimpan tanggal target pengguna.
    fun saveUserTargetDate(targetDate: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_TARGET_DATE, targetDate)
        editor.apply()
    }

    // Fungsi untuk mendapatkan tanggal target pengguna yang disimpan.
    fun getUserTargetDate(): String? {
        return sharedPreferences.getString(KEY_TARGET_DATE, null)
    }
}
