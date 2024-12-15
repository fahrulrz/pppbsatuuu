package com.example.nyobanyoba

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nyobanyoba.adapter.FilmAdapterUser
import com.example.nyobanyoba.database.FilmDao
import com.example.nyobanyoba.database.FilmRoomDatabase
import com.example.nyobanyoba.databinding.FragmentFavoriteBinding
import com.example.nyobanyoba.model.Film
import com.example.nyobanyoba.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavoriteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoriteFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var mFilmDao: FilmDao
    private lateinit var executorService: ExecutorService


    private val filmAdapter by lazy { FilmAdapterUser(mutableListOf()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        executorService = Executors.newSingleThreadExecutor()
        val db = FilmRoomDatabase.getDatabase(requireContext())
        mFilmDao = db!!.filmDao()!!
        with(binding) {
            listFilm.apply {
                adapter = filmAdapter
                layoutManager = GridLayoutManager(
                    this@FavoriteFragment.requireContext(),
                    2,
                    RecyclerView.VERTICAL,
                    false
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getAllFilm()
    }

    private fun getAllFilm(){
        executorService.execute{
            try {
                val films = mFilmDao.getAllFilms() // Mendapatkan semua film dari database
                requireActivity().runOnUiThread {
                    filmAdapter.updateData(films) // Memperbarui adapter dengan data dari database
                }
            } catch (e: Exception) {
                requireActivity().runOnUiThread {
                    // Menangani error jika terjadi
                    Toast.makeText(requireContext(), "Error fetching films: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FavoriteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavoriteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}