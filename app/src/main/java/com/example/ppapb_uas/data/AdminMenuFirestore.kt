package com.example.ppapb_uas.data

// Data class yang merepresentasikan entitas AdminMenu saat digunakan dengan Firestore
data class AdminMenuFirestore(
    val id: String = "",           // ID unik dari dokumen Firestore
    val userId: String = "",       // ID pengguna terkait
    val foodName: String = "",     // Nama makanan
    val foodCalorie: Int = 0,      // Kalori makanan
    val date: String = ""          // Tanggal entri makanan
)
