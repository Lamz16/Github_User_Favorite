package com.lamz.myapplication.ui


import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.lamz.myapplication.R
import com.lamz.myapplication.adapter.SectionsPagerAdapter
import com.lamz.myapplication.databinding.ActivityDetailBinding
import com.lamz.myapplication.model.DetailViewModel
import com.lamz.myapplication.model.SettingViewModel
import com.lamz.myapplication.remote.response.DetailUserResponse
import com.lamz.myapplication.ui.setting.SettingPreferences
import com.lamz.myapplication.ui.setting.SettingViewModelFactory
import com.lamz.myapplication.ui.setting.dataStore
import kotlin.math.log

class DetailActivity : AppCompatActivity() {

    private lateinit var _activityDetailBinding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityDetailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(_activityDetailBinding.root)
        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel = ViewModelProvider(
            this,
            SettingViewModelFactory(pref)
        )[SettingViewModel::class.java]
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = intent.getStringExtra(EXTRA_LOGIN).toString()
        val viewPager: ViewPager2 = _activityDetailBinding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = _activityDetailBinding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()



        supportActionBar?.elevation = 0f

        val login = intent.getStringExtra(EXTRA_LOGIN)

        if (login != null) {
            detailViewModel.getProfile(login)
            detailViewModel.getFollowers(login)
        }


        detailViewModel.userProfile.observe(this) { detailUser ->
            setProfileData(detailUser)
        }

        detailViewModel.followLoading.observe(this) {
            showLoading(it)
        }



    }

    @SuppressLint("SetTextI18n")
    private fun setProfileData(login: DetailUserResponse) {
        _activityDetailBinding.apply {
            tvUsername.text = login.login
            tvFullName.text = login.name
      tvFollower.text = "${login.followers} followers"
      tvFollowing.text = "${login.following} following"
        }
        Glide.with(this).load(login.avatarUrl).into(_activityDetailBinding.imgProfile)
    }

    private fun showLoading(isLoading: Boolean) {
        _activityDetailBinding.progressBarDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_LOGIN = "extra_login"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1, R.string.tab_text_2
        )
    }
}