package com.example.trenserproject

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.trenserproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val galleryIntent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            getImageFromGallery(it.data)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.openGalleryBtn.setOnClickListener {
            openGallery()
        }
    }

    private fun getImageFromGallery(data: Intent?) {
        try {
            data?.data?.let {
                sendDirectUriToWhatsapp(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sendDirectUriToWhatsapp(uri : Uri) {
        try {
            val uriAsString = uri.toString()
            val imageUri = Uri.parse(uriAsString)
            val waIntent = Intent(Intent.ACTION_SEND)
            waIntent.setType("image/*")
            waIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
            waIntent.setPackage("com.whatsapp")
            startActivity(Intent.createChooser(waIntent, "Share with"))
        } catch (e: Exception) {
            println(e.toString())
        }
    }

    private fun openGallery() {
        try {
            val intent = if (Build.VERSION.SDK_INT >= 33) {
                Intent(MediaStore.ACTION_PICK_IMAGES).also {
                    it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
                    it.type = "image/*"
                }
            } else {
                Intent().also {
                    it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
                    it.type = "image/*"
                    it.action = Intent.ACTION_GET_CONTENT
                }
            }
            galleryIntent.launch(intent)
        } catch (e: Exception) {
            println(e.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}