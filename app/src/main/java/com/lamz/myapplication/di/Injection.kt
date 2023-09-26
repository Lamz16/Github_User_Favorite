package com.lamz.myapplication.di

import android.content.Context
import com.lamz.myapplication.data.UserRepository
import com.lamz.myapplication.database.UserDatabase
import com.lamz.myapplication.remote.retrofit.ApiConfig
import com.lamz.myapplication.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserDatabase.getInstance(context)
        val dao = database.favoriteDao()
        val appExecutors = AppExecutors()
        return UserRepository.getInstance(apiService, dao, appExecutors)
    }
}