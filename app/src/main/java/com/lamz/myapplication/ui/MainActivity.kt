package com.lamz.myapplication.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lamz.myapplication.R
import com.lamz.myapplication.adapter.ListProfileAdapter
import com.lamz.myapplication.data.Result
import com.lamz.myapplication.databinding.ActivityMainBinding
import com.lamz.myapplication.model.MainViewModel
import com.lamz.myapplication.model.SettingViewModel
import com.lamz.myapplication.ui.setting.SettingPreferences
import com.lamz.myapplication.ui.setting.SettingViewModelFactory
import com.lamz.myapplication.ui.setting.ThemeActivity
import com.lamz.myapplication.ui.setting.dataStore


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding

    private val mainViewModel by viewModels<MainViewModel>{
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        mainViewModel.Loading.observe(this){
            showLoading(it)
        }

        val userAdapter = ListProfileAdapter{user ->
            if (user.isFavorite){
                mainViewModel.deleteFavorite(user)
            }else{
                mainViewModel.saveFavorite(user)
            }
        }


        /*Get Data from API*/
        mainViewModel.getAllUser("Andi").observe(this) { listUser ->
            if (listUser != null) {
                when (listUser) {
                    is Result.Loading -> {
                        mainViewModel.Loading.observe(this){
                            showLoading(it)
                        }
                    }

                    is Result.Success -> {
                        mainViewModel.Loading.observe(this){
                            showLoading(it)
                        }
                        val userData = listUser.data
                        userAdapter.submitList(userData)

                    }

                    is Result.Error -> {
                        mainViewModel.Loading.observe(this){
                            showLoading(it)
                        }
                        Toast.makeText(
                            this,
                            "terjadi kesalahan" + listUser.error, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding?.rvListUser?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = userAdapter
        }

        /*binding searchView for set query to search user*/
        binding?.searchBar?.inflateMenu(R.menu.option_menu)
        binding?.searchBar?.setOnMenuItemClickListener{menuItem ->
            when(menuItem.itemId){
                R.id.menu1 -> {
                    val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu2 -> {
                    val intent = Intent(this@MainActivity, ThemeActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        binding?.searchView?.setupWithSearchBar(binding?.searchBar)
        binding?.searchView?.editText?.setOnEditorActionListener { textView, _, _ ->
            binding?.searchBar?.text = binding?.searchView?.text
            mainViewModel.getAllUser(binding?.searchView?.text.toString())
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(textView.windowToken, 0)
            binding?.searchView?.hide()
            false
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        binding
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}