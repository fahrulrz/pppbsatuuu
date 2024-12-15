package com.example.nyobanyoba

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.nyobanyoba.auth.LoginActivity
import com.example.nyobanyoba.auth.PrefManager
import com.example.nyobanyoba.databinding.ActivityMainBinding
import com.example.nyobanyoba.model.User
import com.example.nyobanyoba.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager.getInstance(this)
        checkLoginStatus()

        with(binding) {
//            btnAddUser.setOnClickListener{
//                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
//            }

//           menghubungkan bottom navigation dengan fragment
            val navController = findNavController(R.id.nav_host_fragment)
            bottomNavigationView.setupWithNavController(navController)

//            val tvUsers = findViewById<TextView>(R.id.tvUsers)
//            fetchUsers(tvUsers)
        }
    }

//    cek login
    private fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        if (!isLoggedIn) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        } else {
            when (prefManager.getRole()) {
                "admin" -> {
                    startActivity(Intent(this@MainActivity, HomeAdminActivity::class.java))
                    finish()
                }
                "user" -> {
                    // Jangan meluncurkan MainActivity lagi karena sudah berada di MainActivity
                    // Tidak perlu aksi tambahan untuk role "user"
                }
            }
        }
    }


    //  mengambil data dari database firebase
//    private fun fetchUsers(tvUsers: TextView) {
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val response = RetrofitInstance.api.getUsers()
//                if (response.isSuccessful) {
//                    val users = response.body() // Respons sekarang berupa List<User>
//                    val userList = users?.joinToString(separator = "\n") { user ->
//                        "ID: ${user._id}, Name: ${user.name}, Age: ${user.age}"
//                    }
//                    runOnUiThread {
//                        tvUsers.text = userList ?: "No users found"
//                    }
//                } else {
//                    runOnUiThread {
//                        tvUsers.text = "Error fetching users: ${response.errorBody()?.string()}"
//                    }
//                }
//            } catch (e: Exception) {
//                runOnUiThread {
//                    tvUsers.text = "Exception: $e"
//                }
//            }
//        }
//    }
}