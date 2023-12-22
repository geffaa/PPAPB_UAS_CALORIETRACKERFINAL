package com.example.ppapb_uas.MenuUser

import android.app.Dialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.DialogFragment

// Kelas TimePicker yang merupakan turunan dari DialogFragment
class TimePicker : DialogFragment() {

    // Metode onCreateDialog untuk membuat dialog TimePicker
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Mendapatkan instance dari Calendar
        val calendar = Calendar.getInstance()
        // Mendapatkan jam dan menit saat ini
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // Membuat dan mengembalikan TimePickerDialog
        return TimePickerDialog(
            requireActivity(), // Menggunakan aktivitas terkait
            activity as TimePickerDialog.OnTimeSetListener, // Listener saat waktu diatur
            hourOfDay, // Jam saat ini
            minute, // Menit saat ini
            DateFormat.is24HourFormat(activity) // Format waktu menggunakan 24 jam atau tidak
        )
    }
}
