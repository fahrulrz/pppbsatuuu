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
import com.example.nyobanyoba.R
import com.example.nyobanyoba.database.FilmDao
import com.example.nyobanyoba.database.FilmRoomDatabase
import com.example.nyobanyoba.databinding.FilmListUserBinding
import com.example.nyobanyoba.model.FilmResponse
import com.example.nyobanyoba.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FilmAdapterUser(private val listFilm: MutableList<FilmResponse>) : RecyclerView.Adapter<FilmAdapterUser.ItemFilmViewHolder>() {
    private lateinit var mFilmDao: FilmDao
    private lateinit var executorService: ExecutorService
    private lateinit var binding: FilmListUserBinding

    inner class ItemFilmViewHolder(private val binding: FilmListUserBinding) : RecyclerView.ViewHolder(binding.root) {

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

        @SuppressLint("SetTextI18n")
        fun bind(film: FilmResponse) {
            with(binding) {
                txtTitleFilm.text = film.title
                txtMinute.text = film.duration.toString()
                Glide // agar dapat menampilkan gambar dari url
                    .with(binding.root.context)
                    .load(film.image)
                    .centerCrop()
                    .into(fotoFilm);
                if (film.isFavorite) {
                    btnFavorite.setImageResource(R.drawable.bookmark_solid)
                } else {
                    btnFavorite.setImageResource(R.drawable.bookmark_regular)
                }

                // menyimpan data ke storage lokal
                btnFavorite.setOnClickListener{
                    if (!film.isFavorite) {
                        btnFavorite.setImageResource(R.drawable.bookmark_solid)
                        val filmUpdate = FilmResponse(title = film.title, duration = film.duration, synopsis = film.synopsis, image = film.image, isFavorite = true, id = film.id)
                        updateFilm(
                            film.id,
                            filmUpdate,
                            onSuccess = { filmUpdated ->
                                insert(filmUpdate)
                                Log.i("storage lokal", "data berhasil dimasukkan ke storage lokal: $filmUpdated")
                                Log.i("data yang dimasukkan di lokal", film.toString())
                            }, onError = { errorMessage ->
                                Log.i("storage lokal", "data gagal dimasukkan ke storage lokal: $errorMessage")
                            }
                        )

                    } else {
                        btnFavorite.setImageResource(R.drawable.bookmark_regular)
                        val filmUpdate = FilmResponse(title = film.title, duration = film.duration, synopsis = film.synopsis, image = film.image, isFavorite = false, id = film.id)
                        updateFilm(
                            film.id,
                            filmUpdate,
                            onSuccess = { filmUpdated ->
                                delete(film)
                                Log.i("storage lokal", "data berhasil dihapus dari storage lokal: $filmUpdated")
                            }, onError = { errorMessage ->
                                Log.i("storage lokal", "data gagal dihapus dari storage lokal: $errorMessage")
                            }
                        )
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFilmViewHolder {
        binding = FilmListUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val db = FilmRoomDatabase.getDatabase(parent.context)
        executorService = Executors.newSingleThreadExecutor()
        mFilmDao = db!!.filmDao()!!
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
    
    // menyimpan data di storage lokal (Dao)
    private fun insert(film: FilmResponse) {
        executorService.execute { mFilmDao.insertFilm(film) }
    }

    private fun delete(film: FilmResponse) {
        executorService.execute { mFilmDao.deleteFilm(film) }
    }

    private fun checkIfFilmExists(filmResponse: FilmResponse): Boolean {
        return mFilmDao.isFilmExists(filmResponse.id)
    }

    private fun updateFilm(
        id: String,
        film: FilmResponse,
        onSuccess: (FilmResponse?) -> Unit,
        onError: (String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.updateFilm(id, film)
                if (response.isSuccessful) {
                    val updatedFilm = response.body() // Respons berhasil
                    withContext(Dispatchers.Main) {
                        Log.d("Film Update adapter", "Film updated successfully: $updatedFilm")
                        Toast.makeText(binding.root.context, "Success to update film", Toast.LENGTH_SHORT).show()
                        onSuccess(updatedFilm) // Memanggil callback onSuccess
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    withContext(Dispatchers.Main) {
                        Log.e("Film Update", "Failed to update film: $errorBody")
                        Toast.makeText(binding.root.context, "Failed to update film", Toast.LENGTH_SHORT).show()
                        onError(errorBody) // Memanggil callback onError
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("Film Update", "Exception: ${e.message}")
                    Toast.makeText(binding.root.context, "Errore: ${e.message}", Toast.LENGTH_SHORT).show()
                    onError(e.message ?: "Unknown exception") // Memanggil callback onError
                }
            }
        }
    }

}
