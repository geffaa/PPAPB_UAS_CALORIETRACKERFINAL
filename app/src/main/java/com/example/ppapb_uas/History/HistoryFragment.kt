package com.example.ppapb_uas.History

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ppapb_uas.MenuUser.ChooseMenuActivity
import com.example.ppapb_uas.R
import com.example.ppapb_uas.Util.SharedPreferencesHelper
import com.example.ppapb_uas.data.MenuRoomDatabase
import com.example.ppapb_uas.data.UserMenu
import com.example.ppapb_uas.data.UserMenuDAO
import com.example.ppapb_uas.databinding.FragmentHistoryBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var mMenuUserDao: UserMenuDAO
    private lateinit var menuUserAdapter: UserMenuAdapter
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val db = MenuRoomDatabase.getDatabase(requireContext())
        mMenuUserDao = db?.MenuUserDAO() ?: throw Exception("Database not initialized")
        sharedPreferencesHelper = SharedPreferencesHelper.getInstance(requireContext())

        // Set up RecyclerView
        val recyclerView: RecyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Menginisialisasi adapter untuk RecyclerView
        menuUserAdapter = UserMenuAdapter(emptyList(), mMenuUserDao)
        recyclerView.adapter = menuUserAdapter

        // Listener untuk tombol fabAdd
        binding.fabAdd.setOnClickListener {
            // Pindah ke ChooseMenuActivity ketika fabAdd ditekan
            val toMainActivity = Intent(requireContext(), ChooseMenuActivity::class.java)
            startActivity(toMainActivity)
        }

        // Memperbarui tampilan RecyclerView
        updateRecyclerView()

        return root
    }

    // Fungsi untuk menambahkan menu ke database
    private fun insertMenu(menuUser: UserMenu) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                mMenuUserDao.insert(menuUser)
            }
            showToast("Menu berhasil ditambahkan")
        } catch (e: Exception) {
            showToast("Error menambahkan menu")
        }
    }

    // Fungsi untuk menampilkan pesan Toast
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    // Fungsi untuk memperbarui tampilan RecyclerView dengan data terbaru
    private fun updateRecyclerView() {
        mMenuUserDao.allMenus.observe(viewLifecycleOwner, { menuUserList ->
            menuUserAdapter.updateData(menuUserList)
        })
    }

    // Fungsi yang dipanggil ketika Fragment dihancurkan
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
