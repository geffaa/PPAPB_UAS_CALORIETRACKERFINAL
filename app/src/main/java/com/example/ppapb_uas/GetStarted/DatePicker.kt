package com.example.ppapb_uas.GetStarted

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class DatePicker : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Membuat instance dari kelas Calendar untuk mendapatkan tanggal, bulan, dan tahun saat ini
        val calendar =  Calendar.getInstance()
        val year =  calendar.get(Calendar.YEAR)
        val monthofyear =  calendar.get(Calendar.MONTH)
        val dayofmonth = calendar.get(Calendar.DAY_OF_MONTH)

        // Membuat dan mengembalikan objek DatePickerDialog dengan tanggal, bulan, dan tahun saat ini
        return  DatePickerDialog(
            requireActivity(),
            activity as DatePickerDialog.OnDateSetListener, // Listener untuk menangani hasil pemilihan tanggal
            year, monthofyear, dayofmonth
        )
    }
}
