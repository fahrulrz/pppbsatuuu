package com.example.nyobanyoba

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.nyobanyoba.auth.PrefManager
import com.example.nyobanyoba.databinding.ActivityDetailFilmBinding
import com.example.nyobanyoba.model.FilmResponse

class DetailFilmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailFilmBinding
    private lateinit var prefManager: PrefManager

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFilmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)

        val movie = intent.getParcelableExtra("film", FilmResponse::class.java)

        if (movie != null) {
            with(binding) {
                val id = movie.id
                val title = movie.title
                val image = movie.image
                val duration = "${movie.duration} minutes"
                val synopsis = movie.synopsis

                filmTitle.text = title
                txtDuration.text = duration
                txtSynopsis.text = synopsis
                Glide // agar dapat menampilkan gambar dari url
                    .with(binding.root.context)
                    .load(image)
                    .centerCrop()
                    .into(filmCover);

                btnBack.setOnClickListener{

                    when (prefManager.getRole()) {
                        "admin" -> {
                            startActivity(Intent(this@DetailFilmActivity, HomeAdminActivity::class.java))
                            finish()
                        }
                        "user" -> {
                            startActivity(Intent(this@DetailFilmActivity, MainActivity::class.java))
                            finish()
                        }
                    }
                }
            }
        }
    }
}