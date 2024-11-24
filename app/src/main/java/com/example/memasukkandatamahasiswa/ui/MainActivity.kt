package com.example.memasukkandatamahasiswa.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.memasukkandatamahasiswa.R
import com.example.memasukkandatamahasiswa.custom.NoteAdapter
import com.example.memasukkandatamahasiswa.custom.PrefManager
import com.example.memasukkandatamahasiswa.database.Note
import com.example.memasukkandatamahasiswa.database.NoteDao
import com.example.memasukkandatamahasiswa.database.NoteRoomDatabase
import com.example.memasukkandatamahasiswa.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mNotesDao: NoteDao
    private lateinit var executorService: ExecutorService
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.listStudent.layoutManager = LinearLayoutManager(this@MainActivity)
        executorService = Executors.newSingleThreadExecutor()
        val db = NoteRoomDatabase.getDatabase(this)
        mNotesDao = db!!.noteDao()!!

        prefManager = PrefManager.getInstance(this)

        checkLoginStatus()
        with(binding){
            btnAddData.setOnClickListener{
                val intent = Intent(this@MainActivity, AddDataActivity::class.java)
                startActivity(intent)
            }
            btnLogout.setOnClickListener{
                prefManager.setLoggedIn(false)
                startActivity(Intent(this@MainActivity, login_activity::class.java))
                finish()
            }
        }
    }

    fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        if (!isLoggedIn) {
            startActivity(Intent(this@MainActivity, login_activity::class.java))
            finish()
        }
    }

    private fun getAllNote(context: Context) {
        mNotesDao.allNotes.observe(this) { notes ->
            val adapter = NoteAdapter(context, notes)
            binding.listStudent.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        getAllNote(this)
    }

    fun deleteNote(note: Note) {
        executorService.execute { mNotesDao.delete(note) }
        getAllNote(this)
    }
}