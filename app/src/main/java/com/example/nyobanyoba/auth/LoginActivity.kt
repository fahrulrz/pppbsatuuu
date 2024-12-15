package com.example.nyobanyoba.auth

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nyobanyoba.HomeAdminActivity
import com.example.nyobanyoba.MainActivity
import com.example.nyobanyoba.R
import com.example.nyobanyoba.database.UserDao
import com.example.nyobanyoba.database.UserRoomDatabase
import com.example.nyobanyoba.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefManager: PrefManager
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager.getInstance(this)

        val database = UserRoomDatabase.getDatabase(this)
        userDao = database.userDao()

        val swipeUpAnimate = AnimationUtils.loadAnimation(this, R.anim.swipe_up)

        val swipeUpAnimate2 = AnimationUtils.loadAnimation(this, R.anim.swipe_up)
        swipeUpAnimate2.duration = 1200

        val swipeUpAnimate3 = AnimationUtils.loadAnimation(this, R.anim.swipe_up)
        swipeUpAnimate3.duration = 1600

        val swipeUpAnimate4 = AnimationUtils.loadAnimation(this, R.anim.swipe_up)
        swipeUpAnimate4.duration = 2000

        val swipeUpAnimate5 = AnimationUtils.loadAnimation(this, R.anim.swipe_up)
        swipeUpAnimate5.duration = 2400

        val swipeDownAnimate = AnimationUtils.loadAnimation(this, R.anim.swipe_down)


        with(binding){

            title.startAnimation(swipeDownAnimate)
            edtUsername.startAnimation(swipeUpAnimate)
            edtPassword.startAnimation(swipeUpAnimate2)
            btnLoginAdmin.startAnimation(swipeUpAnimate4)
            btnLoginUser.startAnimation(swipeUpAnimate3)
            txtSign.startAnimation(swipeUpAnimate5)
            txtDontHaveAccount.startAnimation(swipeUpAnimate5)

            btnLoginAdmin.setOnClickListener {
                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this@LoginActivity, "Mohon isi semua data", Toast.LENGTH_SHORT
                    ).show()
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val user = userDao.getUserByUsernameAndPassword(username, password)
                        withContext(Dispatchers.Main) {
                            if (user != null && user.role == "admin") {
                                prefManager.setLoggedIn(true)
                                prefManager.saveUsername(username)
                                prefManager.savePassword(password)
                                prefManager.saveRole(user.role)
                                checkLoginStatus()
                            } else {
                                Toast.makeText(this@LoginActivity, "Username atau password admin salah", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

            btnLoginUser.setOnClickListener {
                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this@LoginActivity, "Mohon isi semua data", Toast.LENGTH_SHORT
                    ).show()
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val user = userDao.getUserByUsernameAndPassword(username, password)
                        withContext(Dispatchers.Main) {
                            if (user != null && user.role == "user") {
                                prefManager.setLoggedIn(true)
                                prefManager.saveUsername(username)
                                prefManager.savePassword(password)
                                prefManager.saveRole(user.role)
                                checkLoginStatus()
                            } else {
                                Toast.makeText(this@LoginActivity, "Username atau password user salah", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

            txtSign.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }

    private fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        if (isLoggedIn) {
            if (prefManager.getRole() == "admin") {
                Toast.makeText(this@LoginActivity, "Login berhasil", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@LoginActivity, HomeAdminActivity::class.java))
            } else if (prefManager.getRole()=="user") {
                Toast.makeText(this@LoginActivity, "Login berhasil", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        } else {
            Toast.makeText(this@LoginActivity, "Login gagal",
                Toast.LENGTH_SHORT).show()
        }
    }
}