package com.example.memasukkandatamahasiswa.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.memasukkandatamahasiswa.R
import com.example.memasukkandatamahasiswa.databinding.ActivityDetailMahasiwaBinding

class DetailMahasiwa : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMahasiwaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMahasiwaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val noteId = intent.getIntExtra("NOTE_ID", -1)
        val noteName = intent.getStringExtra("NOTE_NAME")
        val noteNim = intent.getStringExtra("NOTE_NIM")
        val noteGender = intent.getStringExtra("NOTE_GENDER")
        val noteAddress = intent.getStringExtra("NOTE_ADDRESS")

        binding.name.text = noteName
        binding.nim.text = noteNim
        binding.address.text = noteAddress
        when (noteGender) {
            "Laki-laki" -> binding.gender.text = "Lanang"
            "Perempuan" -> binding.gender.text = "Cewe"
            else -> binding.gender.text = "Anomali"
        }

        with(binding){
            btnBack.setOnClickListener{
                val intent = Intent(this@DetailMahasiwa, MainActivity::class.java)
                startActivity(intent)
            }
        }

    }
}