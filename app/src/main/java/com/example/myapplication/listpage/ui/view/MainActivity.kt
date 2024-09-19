package com.example.myapplication.ui.view

import ApiService
import PhotoAdapter
import PhotoRepository
import PhotoViewModel
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.utils.RetrofitClient

class MainActivity : AppCompatActivity() {

    private lateinit var photoViewModel: PhotoViewModel
    private lateinit var adapter: PhotoAdapter
    private lateinit var mainBinding: ActivityMainBinding
    private val apiService = RetrofitClient.getRetrofitInstance().create(ApiService::class.java)

    private val repository = PhotoRepository(apiService)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        setup()

    }

    private fun setup() {
        mainBinding.recyclerView.layoutManager = LinearLayoutManager(this)

        photoViewModel = PhotoViewModel(repository)

        photoViewModel.photos.observe(this) { photos ->
                mainBinding.progressBarCyclic.visibility= View.GONE
            adapter = PhotoAdapter(photos)
            mainBinding.recyclerView.adapter = adapter
        }
    }
}
