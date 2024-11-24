package com.example.memasukkandatamahasiswa.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.memasukkandatamahasiswa.R
import com.example.memasukkandatamahasiswa.database.Note
import com.example.memasukkandatamahasiswa.database.NoteDao
import com.example.memasukkandatamahasiswa.database.NoteRoomDatabase
import com.example.memasukkandatamahasiswa.databinding.ActivityAddDataBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AddDataActivity : AppCompatActivity() {
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

        with(binding){
            btnSave.setOnClickListener(View.OnClickListener {
                val selectedGender: String = when (radioButtonGender.checkedRadioButtonId){
                    R.id.radio_button_male -> "Laki-laki"
                    R.id.radio_button_female -> "Perempuan"
                    else -> "Anomali"
                }
                insert(
                    Note(
                        name = editeName.text.toString(),
                        nim = editNim.text.toString(),
                        gender = selectedGender,
                        address = editAddress.text.toString()
                    )
                )
                val intent = Intent(this@AddDataActivity, MainActivity::class.java)
                startActivity(intent)
            })
        }
    }

    private fun insert(note: Note) {
        executorService.execute { mNotesDao.insert(note) }
    }
}