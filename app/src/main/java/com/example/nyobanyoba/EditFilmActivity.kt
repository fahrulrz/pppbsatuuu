package com.example.nyobanyoba

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.nyobanyoba.databinding.ActivityDetailFilmBinding
import com.example.nyobanyoba.databinding.ActivityEditFilmBinding
import com.example.nyobanyoba.model.Film
import com.example.nyobanyoba.model.FilmResponse
import com.example.nyobanyoba.network.CloudinaryApi
import com.example.nyobanyoba.network.FirebaseApi
import com.example.nyobanyoba.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class EditFilmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditFilmBinding

    private val PICK_IMAGE_REQUEST = 1 // Request code untuk intent memilih gambar
    private lateinit var imgPreview: ImageView // ImageView untuk menampilkan gambar preview
    private var selectedImageUri: Uri? = null // URI gambar yang dipilih pengguna

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditFilmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movie = intent.getParcelableExtra("film", FilmResponse::class.java)

        imgPreview = binding.imgPreview // Hubungkan ImageView global

        with(binding) {
            if (movie != null) {
                // Tombol untuk memilih gambar
                imgPreview.setOnClickListener {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*" // Memfilter hanya gambar
                    startActivityForResult(intent, PICK_IMAGE_REQUEST)
                }

                edtFilmTitle.setText(movie.title)
                edtDuration.setText(movie.duration.toString())
                edtSynopsis.setText(movie.synopsis)
                Glide // agar dapat menampilkan gambar dari url
                    .with(binding.root.context)
                    .load(movie.image)
                    .centerCrop()
                    .into(imgPreview);
                selectedImageUri = movie.image.toUri()

                btnDone.setOnClickListener{
                    val newTitle = edtFilmTitle.text.toString()
                    val newDuration = edtDuration.text.toString().toInt()
                    val newSynopsis = edtSynopsis.text.toString()

                    if (newTitle.isNotEmpty() && newDuration != null && newSynopsis.isNotEmpty() && selectedImageUri != null) {
                        if (selectedImageUri == movie.image.toUri()) {
                            val film = FilmResponse(title = newTitle, duration = newDuration, synopsis = newSynopsis, image = movie.image, id = movie.id)
                            updateFilm(movie.id, film)
                        } else {
                            uploadImageToCloudinary(selectedImageUri!!, { imageUrl ->
                                val film = FilmResponse(title = newTitle, duration = newDuration, synopsis = newSynopsis, image = imageUrl, id = movie.id)
                                updateFilm(movie.id, film)
                            }, { errorMessage ->
                                Toast.makeText(this@EditFilmActivity, errorMessage, Toast.LENGTH_SHORT).show()
                            })
                        }
                    } else {
                        Toast.makeText(this@EditFilmActivity, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data // Simpan URI gambar yang dipilih
            selectedImageUri?.let { uri ->
                val bitmap = getBitmapFromUri(uri) // Konversi URI ke Bitmap
                imgPreview.setImageBitmap(bitmap) // Tampilkan gambar di ImageView
            }
        }
    }

    // Mengambil Bitmap dari URI
    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Simpan gambar ke Cloudinary
    private fun uploadImageToCloudinary(uri: Uri, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Convert URI ke File
                val inputStream = contentResolver.openInputStream(uri)
                val tempFile = File.createTempFile("upload_", ".jpg")
                inputStream.use { input ->
                    tempFile.outputStream().use { output ->
                        input?.copyTo(output)
                    }
                }

                // Persiapkan MultipartBody untuk gambar
                val requestBody = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
                val multipartBody = MultipartBody.Part.createFormData("file", tempFile.name, requestBody)

                // Upload preset Cloudinary (sesuaikan dengan konfigurasi Anda di Cloudinary)
                val uploadPreset = "ml_default"

                // Panggil API upload
                val response = CloudinaryApi.cloudinaryService.uploadImage(multipartBody, uploadPreset)
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string() // Ambil body dari response sebagai string
                    if (responseBody != null) {
                        Log.d("Cloudinary_Response", responseBody)
                        try {
                            // Parsing response body menggunakan JSONObject
                            val jsonResponse = JSONObject(responseBody)
                            val imageUrl = jsonResponse.getString("secure_url") // Ambil URL gambar
                            runOnUiThread {
                                onSuccess(imageUrl)
                            }
                        } catch (e: JSONException) {
                            Log.e("Cloudinary_Exception", "Error parsing JSON: ${e.message}")
                            runOnUiThread {
                                onError("JSON Parsing Error")
                            }
                        }
                    } else {
                        Log.e("Cloudinary_Error", "Response body is null")
                        runOnUiThread {
                            onError("Response body is null")
                        }
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("Cloudinary_Error", error ?: "Unknown error")
                    runOnUiThread {
                        onError("Upload failed: ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                Log.e("Cloudinary_Exception", e.toString())
                runOnUiThread {
                    onError("Exception: $e")
                }
            }
        }
    }


    // Membuat file sementara dari URI
    private fun createTempFileFromUri(uri: Uri): File {
        val inputStream = contentResolver.openInputStream(uri)
        val file = File.createTempFile("upload", ".jpg", cacheDir)
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file
    }

    private fun updateFilm(id: String, film: FilmResponse) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.updateFilm(id, film)
                if (response.isSuccessful) {
                    val updatedFilm = response.body() // Respons berhasil
                    runOnUiThread {
                        Log.d("Film Update", "Film updated successfully: $updatedFilm")
                        Toast.makeText(this@EditFilmActivity, "Film updated!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("Film Update", "Failed to update film: $errorBody")
                    runOnUiThread {
                        Toast.makeText(this@EditFilmActivity, "Failed to update film", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("Film Update", "Exception: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@EditFilmActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}