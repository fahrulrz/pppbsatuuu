package com.example.memasukkandatamahasiswa.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.memasukkandatamahasiswa.R
import com.example.memasukkandatamahasiswa.database.Note
import com.example.memasukkandatamahasiswa.database.NoteDao
import com.example.memasukkandatamahasiswa.database.NoteRoomDatabase
import com.example.memasukkandatamahasiswa.databinding.ActivityAddDataBinding
import com.example.memasukkandatamahasiswa.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class EditDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddDataBinding
    private lateinit var mNotesDao: NoteDao
    private lateinit var executorService: ExecutorService
    private var updateId: Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        executorService = Executors.newSingleThreadExecutor()
        val db = NoteRoomDatabase.getDatabase(this)
        mNotesDao = db!!.noteDao()!!

        val noteId = intent.getIntExtra("NOTE_ID", -1)
        val noteName = intent.getStringExtra("NOTE_NAME")
        val noteNim = intent.getStringExtra("NOTE_NIM")
        val noteGender = intent.getStringExtra("NOTE_GENDER")
        val noteAddress = intent.getStringExtra("NOTE_ADDRESS")

        binding.editeName.setText(noteName)
        binding.editNim.setText(noteNim)
        binding.editAddress.setText(noteAddress)
        when (noteGender) {
            "Laki-laki" -> binding.radioButtonMale.isChecked = true
            "Perempuan" -> binding.radioButtonFemale.isChecked = true
        }

        binding.btnSave.setOnClickListener{
            val selectedGender: String = when (binding.radioButtonGender.checkedRadioButtonId){
                R.id.radio_button_male -> "Laki-laki"
                R.id.radio_button_female -> "Perempuan"
                else -> "Anomali"
            }
            update(
                Note(
                    id = noteId,
                    name = binding.editeName.text.toString(),
                    nim = binding.editNim.text.toString(),
                    address = binding.editAddress.text.toString(),
                    gender = selectedGender
                )
            )
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun update(note: Note) {
        executorService.execute { mNotesDao.update(note) }
    }
}