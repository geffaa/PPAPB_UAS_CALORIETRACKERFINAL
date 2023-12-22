package com.example.ppapb_uas.Profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// Kelas ProfilesViewModel adalah bagian dari arsitektur Android's ViewModel yang bertanggung jawab untuk menyediakan data yang diperlukan oleh fragment Profil.
class ProfilesViewModel : ViewModel() {

    // MutableLiveData yang menyimpan teks untuk ditampilkan di fragment Profil
    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }

    // LiveData yang dapat di-observe oleh fragment Profil untuk mendapatkan teks
    val text: LiveData<String> = _text
}
