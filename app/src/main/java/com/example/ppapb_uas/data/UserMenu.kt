package com.example.ppapb_uas.data

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "usermenu_table")
// Data class untuk entitas UserMenu yang akan disimpan di database Room
data class UserMenu(

    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,

    // ID pengguna dari Firebase Auth
    var userId: String = "",

    // Menambahkan field baru
    val type: String = "", // "lunch", "dinner", "breakfast"
    val action: String = "", // "makan", "workout"
    val foodName: String = "",
    val foodCalorie: Int = 0,
    val serving: Int = 0,
    val date: String = "",
    val time: String = ""
) : Serializable
