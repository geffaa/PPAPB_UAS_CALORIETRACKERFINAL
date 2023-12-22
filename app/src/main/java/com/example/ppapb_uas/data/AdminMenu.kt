package com.example.ppapb_uas.data

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

// Model data untuk entitas AdminMenu dalam database lokal menggunakan Room
@Entity(tableName = "adminmenu_table")
data class AdminMenu(

    // ID otomatis yang dihasilkan oleh Room
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,

    // ID pengguna yang diperoleh dari Firebase Auth
    var userId: String = "",

    // Field baru yang ditambahkan
    val action: String = "", // "makan", "workout"
    val foodName: String = "",
    val foodCalorie: Int = 0,
    val date: String = "",

    ) : Serializable
