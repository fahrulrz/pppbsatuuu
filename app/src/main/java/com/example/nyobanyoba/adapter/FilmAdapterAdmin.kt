package com.example.nyobanyoba.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nyobanyoba.DetailFilmActivity
import com.example.nyobanyoba.EditFilmActivity
import com.example.nyobanyoba.databinding.FilmListAdminBinding
import com.example.nyobanyoba.model.Film
import com.example.nyobanyoba.model.FilmResponse
import com.example.nyobanyoba.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FilmAdapterAdmin(private val listFilm: MutableList<FilmResponse>) :
    RecyclerView.Adapter<FilmAdapterAdmin.ItemFilmViewHolder>() {
    inner class ItemFilmViewHolder(private val binding: FilmListAdminBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val intent = Intent(binding.root.context, DetailFilmActivity::class.java)
                    intent.putExtra("film", listFilm[position]) // mengirim data ke detail film
                    binding.root.context.startActivity(intent)
                }
            }
        }

        fun bind(film: FilmResponse) {
            with(binding) {
                txtTitleFilm.text = film.title
                Glide // agar dapat menampilkan gambar dari url
                    .with(binding.root.context)
                    .load(film.image)
                    .centerCrop()
                    .into(fotoFilm);

                btnEdit.setOnClickListener{
                    val intent = Intent(binding.root.context, EditFilmActivity::class.java)
                    intent.putExtra("film", film)
                    binding.root.context.startActivity(intent)
                }

                btnTrash.setOnClickListener{
                    deleteFilm(film.id)
                }
            }
        }

        private fun deleteFilm(id: String) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Mengirimkan permintaan untuk menghapus film berdasarkan ID
                    val response = RetrofitInstance.api.deleteFilm(id)

                    if (response.isSuccessful) {
                        // Jika berhasil dihapus
                            Log.d("Film Delete", "Film deleted successfully")
//                            Toast.makeText(binding.root.context, "Film deleted", Toast.LENGTH_SHORT).show()
                            // Lakukan navigasi atau tindakan lainnya setelah penghapusan, misalnya kembali ke halaman sebelumnya
                    } else {
                        // Jika gagal menghapus
                        val errorBody = response.errorBody()?.string()
                        Log.e("Film Delete", "Failed to delete film: $errorBody")
//                            Toast.makeText(binding.root.context, "Failed to delete film", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // Menangani exception jika terjadi error
                    Log.e("Film Delete", "Error: ${e.message}")
//                        Toast.makeText(binding.root.context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFilmViewHolder {
        val binding =
            FilmListAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemFilmViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listFilm.size
    }

    override fun onBindViewHolder(holder: ItemFilmViewHolder, position: Int) {
        holder.bind(listFilm[position])
    }

    // Fungsi untuk memperbarui data
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<FilmResponse>) {
        listFilm.clear()
        listFilm.addAll(newList)
        notifyDataSetChanged() // Memberi tahu RecyclerView untuk memperbarui tampilan
    }
}