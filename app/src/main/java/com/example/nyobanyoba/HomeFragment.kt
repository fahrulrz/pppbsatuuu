package com.example.nyobanyoba

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nyobanyoba.adapter.FilmAdapterAdmin
import com.example.nyobanyoba.adapter.FilmAdapterUser
import com.example.nyobanyoba.databinding.FragmentHomeBinding
import com.example.nyobanyoba.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            listFilmm.apply {
                adapter = filmAdapter
                layoutManager = GridLayoutManager(
                    this@HomeFragment.requireContext(),
                    2,
                    RecyclerView.VERTICAL,
                    false
                )
            }

//            addOnItemTouchListener(
//                RecyclerItemClickListener(
//                    requireContext(),
//                    this
//                ) {
//                    val film = filmAdapter.listFilm[it]
//                    handleItemClick(film)
//                }
//            )

//            btnAddUser.setOnClickListener{
//                val intent = Intent(requireContext(), AddUserActivity::class.java)
//                startActivity(intent)
//            }
//            val action = homeDirections.actionHome2ToCheckoutFragment2(txtContohProductName.text.toString())
//            btnCheckout.setOnClickListener{
//                findNavController().navigate(action)
//            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    private fun fetchData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.getFilms()
                if (response.isSuccessful) {
                    val films = response.body() ?: emptyList()
                    Log.i("isi film blablabla user", films.toString())

                    // Perbarui data di adapter
                    withContext(Dispatchers.Main) {
                        filmAdapter.updateData(films)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Exception: $e", Toast.LENGTH_SHORT).show()
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
         * @return A new instance of fragment home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}