package com.example.ppapb_uas.Admin

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.ppapb_uas.R
import com.example.ppapb_uas.data.AdminMenuFirestore
import com.google.firebase.firestore.FirebaseFirestore

class AdminAdapter : RecyclerView.Adapter<AdminAdapter.AdminDataViewHolder>() {

    // Data daftar makanan dari Firestore
    private var makanan: List<AdminMenuFirestore> = listOf()

    // Firebase Firestore instance dan reference ke koleksi makanan
    private val firestore = FirebaseFirestore.getInstance()
    private val makananCollection = firestore.collection("makanan")

    // Membuat tampilan baru untuk setiap item makanan
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminDataViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin, parent, false)
        return AdminDataViewHolder(view)
    }

    // Menghubungkan data makanan dengan tampilan yang sesuai
    override fun onBindViewHolder(holder: AdminDataViewHolder, position: Int) {
        val currentMakanan = makanan[position]

        // Menetapkan data ke elemen UI
        holder.textViewBuku.text = currentMakanan.foodName
        holder.textViewCalorie.text = currentMakanan.foodCalorie.toString()  +" Cal"

        // Menambahkan onClickListener untuk tombol Edit
        holder.btEdit.setOnClickListener{
            try {
                // Membuka layar EditFoodActivity dengan data makanan yang dipilih
                val intent = Intent(holder.itemView.context, AdminEditFoodActivity::class.java)
                intent.putExtra("id", currentMakanan.id)
                intent.putExtra("foodName", currentMakanan.foodName)
                intent.putExtra("foodCalorie", currentMakanan.foodCalorie)
                holder.itemView.context.startActivity(intent)
            } catch (e: Exception){
                // Menampilkan pesan error jika terjadi kesalahan
                showToast(e.toString(), holder)
                Log.d("ERR", e.toString())
            }
        }

        // Menambahkan onClickListener untuk tombol Delete
        holder.btDelete.setOnClickListener {
            // Menampilkan konfirmasi hapus dengan dialog Yes/No
            showYesNoAlertDialog(
                holder.itemView.context,
                "Apakah anda yakin akan menghapus ${currentMakanan.foodName}",
                DialogInterface.OnClickListener { _, _ ->
                    deleteMakanan(currentMakanan.id, holder)
                }
            )
        }
    }

    // Menghapus makanan dari Firestore berdasarkan ID
    private fun deleteMakanan(id: String, holder: AdminDataViewHolder) {
        makananCollection.document(id)
            .delete()
            .addOnSuccessListener {
                // Menampilkan pesan berhasil jika penghapusan berhasil
                Toast.makeText(
                    holder.itemView.context,
                    "Makanan berhasil dihapus",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                // Menampilkan pesan error jika terjadi kesalahan
                Toast.makeText(
                    holder.itemView.context,
                    "Error deleting document: $e",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    // Mengembalikan jumlah total item makanan dalam daftar
    override fun getItemCount(): Int {
        return makanan.size
    }

    // Menetapkan daftar makanan baru dan melakukan filter berdasarkan query pencarian
    fun setMakanan(makanan: List<AdminMenuFirestore>, searchQuery: String = "") {
        this.makanan = if (searchQuery.isNotEmpty()) {
            makanan.filter { it.foodName.contains(searchQuery, ignoreCase = true) }
        } else {
            makanan
        }

        // Memberi tahu adapter bahwa data telah berubah
        notifyDataSetChanged()
    }

    // Kelas ViewHolder untuk merepresentasikan elemen tampilan setiap item makanan
    inner class AdminDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewBuku: TextView = itemView.findViewById(R.id.item_admin_FoodName)
        val textViewCalorie: TextView = itemView.findViewById(R.id.item_admin_Calorie)
        val btDelete : Button = itemView.findViewById(R.id.item_admin_Delete)
        val btEdit : Button = itemView.findViewById(R.id.item_admin_edit)
    }

    // Menampilkan dialog konfirmasi Yes/No
    fun showYesNoAlertDialog(
        context: Context,
        message: String,
        onYesClickListener: DialogInterface.OnClickListener
    ) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setCancelable(false)

        alertDialogBuilder.setPositiveButton("Yes", onYesClickListener)
        alertDialogBuilder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    // Menampilkan pesan toast
    private fun showToast(message: String, holder: AdminDataViewHolder) {
        Toast.makeText(holder.itemView.context, message, Toast.LENGTH_SHORT).show()
    }
}
