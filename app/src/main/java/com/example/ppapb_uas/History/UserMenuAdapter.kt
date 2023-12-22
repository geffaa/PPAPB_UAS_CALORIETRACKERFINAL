package com.example.ppapb_uas.History

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ppapb_uas.R
import com.example.ppapb_uas.data.UserMenu
import com.example.ppapb_uas.data.UserMenuDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Adapter untuk RecyclerView yang menangani tampilan item riwayat menu
class UserMenuAdapter(var menuUserList: List<UserMenu>, private val menuUserDao: UserMenuDAO) :
    RecyclerView.Adapter<UserMenuAdapter.ViewHolder>() {

    // ViewHolder untuk menyimpan referensi tampilan item dalam RecyclerView
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewFoodName: TextView = itemView.findViewById(R.id.item_History_FoodName)
        val textViewCalorie: TextView = itemView.findViewById(R.id.item_History_Calorie)
        val textViewType: TextView = itemView.findViewById(R.id.item_History_Type)
        val btDelete: Button = itemView.findViewById(R.id.item_History_Delete)
    }

    // Metode yang dipanggil ketika RecyclerView memerlukan ViewHolder baru
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return ViewHolder(itemView)
    }

    // Metode yang dipanggil untuk mengikat data pada posisi tertentu ke ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = menuUserList[position]

        // Menetapkan nilai ke tampilan item
        holder.textViewType.text = currentItem.type
        holder.textViewFoodName.text = currentItem.foodName
        holder.textViewCalorie.text = "Calorie: ${currentItem.foodCalorie}"

        // Menambahkan listener untuk tombol hapus
        holder.btDelete.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(holder.itemView.context)
            alertDialogBuilder.setTitle("Konfirmasi")
            alertDialogBuilder.setMessage("Apakah Anda yakin ingin menghapus item ini?")

            alertDialogBuilder.setPositiveButton("Ya") { dialog, _ ->
                // Pengguna klik Ya, lanjutkan dengan penghapusan
                CoroutineScope(Dispatchers.IO).launch {
                    // Panggil metode delete dari DAO
                    menuUserDao.delete(currentItem)

                    // Kembali ke thread utama untuk menampilkan pesan Toast (opsional)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            holder.itemView.context,
                            "Item dihapus",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                dialog.dismiss()
            }

            alertDialogBuilder.setNegativeButton("Tidak") { dialog, _ ->
                // Pengguna klik Tidak, tidak lakukan apa-apa
                dialog.dismiss()
            }

            // Membuat dan menampilkan dialog konfirmasi penghapusan
            val alertDialog: AlertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

    // Metode untuk memperbarui data yang ditampilkan oleh adapter
    fun updateData(newMenuUserList: List<UserMenu>) {
        menuUserList = newMenuUserList
        notifyDataSetChanged()
    }

    // Metode yang memberikan jumlah total item dalam dataset
    override fun getItemCount() = menuUserList.size
}
