package com.example.memasukkandatamahasiswa.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.memasukkandatamahasiswa.R
import com.example.memasukkandatamahasiswa.custom.PrefManager
import com.example.memasukkandatamahasiswa.databinding.ActivityLoginBinding

class login_activity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager.getInstance(this)
        with(binding){
            btnLogin.setOnClickListener {
                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        this@login_activity,
                        "Mohon isi semua data",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (isValidUsernamePassword()) {
                        prefManager.setLoggedIn(true)
                        checkLoginStatus()
                    } else {
                        Toast.makeText(this@login_activity, "Username atau password salah", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            btnRegister.setOnClickListener {
                startActivity(Intent(this@login_activity, RegisterActivity::class.java))
            }
        }
    }
    private fun isValidUsernamePassword(): Boolean{
        val username = prefManager.getUsername()
        val password = prefManager.getPassword()
        val inputUsername = binding.edtUsername.text.toString()
        val inputPassword = binding.edtPassword.text.toString()
        return username == inputUsername && password == inputPassword
    }
    private fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        if (isLoggedIn) {
            Toast.makeText(this@login_activity, "Login berhasil",
                Toast.LENGTH_SHORT).show()
            startActivity(
                Intent(this@login_activity,
                MainActivity::class.java)
            )
            finish()
        } else {
            Toast.makeText(this@login_activity, "Login gagal",
                Toast.LENGTH_SHORT).show()
        }

    }
}