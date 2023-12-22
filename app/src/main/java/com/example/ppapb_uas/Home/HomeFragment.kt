package com.example.ppapb_uas.Home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import java.util.Date
import java.text.SimpleDateFormat
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.ppapb_uas.BottomNavActivity
import com.example.ppapb_uas.R
import com.example.ppapb_uas.Util.SharedPreferencesHelper
import com.example.ppapb_uas.databinding.FragmentHomeBinding
import java.util.Calendar
import java.util.TimeZone

// Fragment untuk menampilkan beranda aplikasi
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var currentDate: String // Menambahkan deklarasi variabel currentDate

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        sharedPreferencesHelper = SharedPreferencesHelper.getInstance(requireContext())

        val beratSekarang = sharedPreferencesHelper.getUserWeight().toString()
        val beratTarget = sharedPreferencesHelper.getUserTargetWeight().toString()

        binding.hmTvBeratsekarang.text = beratSekarang
        binding.hmTvTargetberat.text = beratTarget

        val tujuanDietTextView = binding.hmTvTujuandiet
        val targetKaloriTextView = binding.hmTvTargetkalori
        val tglTargetTextView = binding.hmTvTgltarget

        val tvTujuanDiet = binding.hmTvTujuandiet
        tvTujuanDiet.text = tvTujuanDiet.text.toString() + sharedPreferencesHelper.getUserTujuanDiet()

        val tujuanDiet = sharedPreferencesHelper.getUserTujuanDiet()
        val maksimumKalori = sharedPreferencesHelper.getUserCalorie().toString()
        val tanggalTarget = sharedPreferencesHelper.getUserTargetDate()

        tujuanDietTextView.text = tujuanDiet
        targetKaloriTextView.text = maksimumKalori
        tglTargetTextView.text = tanggalTarget

        val tanggalHariIniTextView = binding.txtDateNow
        currentDate = getCurrentDateInGMTPlus7()
        tanggalHariIniTextView.text = SimpleDateFormat("EEEE, dd MMMM yyyy").format(Date())

        binding.imageButtonNext.setOnClickListener {
            currentDate = getNextDate(currentDate)
            updateDataForDate(currentDate)
            tanggalHariIniTextView.text = SimpleDateFormat("EEEE, dd MMMM yyyy").format(Date())
        }

        binding.imageButtonBack.setOnClickListener {
            currentDate = getPreviousDate(currentDate)
            updateDataForDate(currentDate)
            tanggalHariIniTextView.text = SimpleDateFormat("EEEE, dd MMMM yyyy").format(Date())
        }

        //GET CALORIE DATA
        val userId = sharedPreferencesHelper.getUserId().toString()
        val targetCalorie = sharedPreferencesHelper.getUserCalorie()
        homeViewModel.getTotalCaloriesForCurrentUserAndDate(userId, getCurrentDateInGMTPlus7())
            .observe(viewLifecycleOwner) { totalCalories ->

                val totalCalories = totalCalories?.takeIf { it >= 0 } ?: 0

                val calLeft = targetCalorie - totalCalories
                val persenPB: Double = (calLeft.toDouble() / targetCalorie.toDouble()) * 100

                binding.progressCircularIndicator.setProgress(persenPB.toInt(), true)

                if (persenPB >= 100.0) {
                    (requireActivity() as BottomNavActivity).createNotification()
                }

                val displayedCalories = calLeft?.takeIf { it >= 0 } ?: 0
                binding.frHomeCalLeft.text = "$displayedCalories"
            }

        //GET CAL LUNCH
        homeViewModel.getCaloriesByType(userId, getCurrentDateInGMTPlus7(), "lunch")
            .observe(viewLifecycleOwner) { lunchCalories ->
                val displayedLunchCalories = lunchCalories?.takeIf { it >= 0 } ?: 0
                binding.frHomeTvLunch.text = "$displayedLunchCalories cal"
            }

        //GET CAL BREAKFAST
        homeViewModel.getCaloriesByType(userId, getCurrentDateInGMTPlus7(), "breakfast")
            .observe(viewLifecycleOwner) { breakfastCalories ->
                val displayedBreakfastCalories = breakfastCalories?.takeIf { it >= 0 } ?: 0
                binding.frHomeTvBreakfast.text = "$displayedBreakfastCalories cal"
            }

        //GET CAL DINNER
        homeViewModel.getCaloriesByType(userId, getCurrentDateInGMTPlus7(), "dinner")
            .observe(viewLifecycleOwner) { dinnerCalories ->
                val displayedDinnerCalories = dinnerCalories?.takeIf { it >= 0 } ?: 0
                binding.frHomeTvDinner.text = "$displayedDinnerCalories cal"
            }

        //SET TEXT FROM SHARED PREF
        binding.frHomeTvName.text = sharedPreferencesHelper.getUserName().toString()
        binding.frHomeCalLeft.text =
            "${sharedPreferencesHelper.getUserCalorie()} \nCal\nLeft"

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Metode untuk mendapatkan tanggal hari ini dengan zona waktu GMT+7
    fun getCurrentDateInGMTPlus7(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        dateFormat.timeZone = TimeZone.getTimeZone("GMT+7")
        return dateFormat.format(Date())
    }

    // Metode untuk mendapatkan tanggal berikutnya dari tanggal yang diberikan
    private fun getNextDate(currentDate: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date = dateFormat.parse(currentDate)
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DATE, 1)
        return dateFormat.format(calendar.time)
    }

    // Metode untuk mendapatkan tanggal sebelumnya dari tanggal yang diberikan
    private fun getPreviousDate(currentDate: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date = dateFormat.parse(currentDate)
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DATE, -1)
        return dateFormat.format(calendar.time)
    }

    // Metode untuk memperbarui data berdasarkan tanggal yang baru
    private fun updateDataForDate(date: String) {
        // Panggil metode yang diperlukan untuk memperbarui data berdasarkan tanggal yang baru.
        // Implementasikan logika ini sesuai kebutuhan Anda.
    }
}
