package com.lamz.myapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lamz.myapplication.adapter.ListProfileAdapter
import com.lamz.myapplication.databinding.ActivityFavoriteBinding
import com.lamz.myapplication.model.MainViewModel

class FavoriteActivity : AppCompatActivity(){

    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val mainViewModel: MainViewModel by viewModels {
            factory
        }
        val userAdapter = ListProfileAdapter { user ->
            if (user.isFavorite) {
                mainViewModel.deleteFavorite(user)
                Toast.makeText(this, "Berhasil Menghapus ${user.username} dari favorite", Toast.LENGTH_SHORT).show()
            }
        }

        /*Get Data from API*/
        mainViewModel.getFavoriteUser().observe(this) { favoritedList ->
            userAdapter.submitList(favoritedList)
        }
        binding.rvFavorite.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = userAdapter
        }

        binding.btnBack.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}