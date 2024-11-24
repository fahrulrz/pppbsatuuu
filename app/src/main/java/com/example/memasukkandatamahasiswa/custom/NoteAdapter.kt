package com.example.memasukkandatamahasiswa.custom

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.memasukkandatamahasiswa.R
import com.example.memasukkandatamahasiswa.database.Note
import com.example.memasukkandatamahasiswa.ui.DetailMahasiwa
import com.example.memasukkandatamahasiswa.ui.EditDataActivity
import com.example.memasukkandatamahasiswa.ui.MainActivity

class NoteAdapter(private val context: Context, private val notes: List<Note>) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    
    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deleteButton: View = itemView.findViewById(R.id.btn_trash)
        val editButton: View = itemView.findViewById(R.id.btn_edit)
        val detailButton: View = itemView.findViewById(R.id.btn_detail)
        val idTextView: TextView = itemView.findViewById(R.id.txt_number)
        val nameTextView: TextView = itemView.findViewById(R.id.txt_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_list_mahastudent, parent, false)
        return NoteViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = notes[position]
        holder.idTextView.text = (holder.adapterPosition + 1).toString() // untuk urutan data recycle
        holder.nameTextView.text = currentNote.name // untuk menampilkan nama

        holder.deleteButton.setOnClickListener {
            (context as MainActivity).deleteNote(notes[holder.adapterPosition])
        }

        holder.editButton.setOnClickListener {
            val intent = Intent(context, EditDataActivity::class.java)
            intent.putExtra("NOTE_ID", currentNote.id)
            intent.putExtra("NOTE_NAME", currentNote.name)
            intent.putExtra("NOTE_NIM", currentNote.nim)
            intent.putExtra("NOTE_GENDER", currentNote.gender)
            intent.putExtra("NOTE_ADDRESS", currentNote.address)
            context.startActivity(intent)
        }

        holder.detailButton.setOnClickListener {
            val intent = Intent(context, DetailMahasiwa::class.java)
            intent.putExtra("NOTE_ID", currentNote.id)
            intent.putExtra("NOTE_NAME", currentNote.name)
            intent.putExtra("NOTE_NIM", currentNote.nim)
            intent.putExtra("NOTE_GENDER", currentNote.gender)
            intent.putExtra("NOTE_ADDRESS", currentNote.address)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }
}