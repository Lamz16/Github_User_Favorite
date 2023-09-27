package com.lamz.myapplication.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lamz.myapplication.remote.response.DetailUserResponse
import com.lamz.myapplication.remote.response.ItemsItem
import com.lamz.myapplication.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    private val _userProfile = MutableLiveData<DetailUserResponse>()
    val userProfile: LiveData<DetailUserResponse> = _userProfile

    private val _listFollower = MutableLiveData<List<ItemsItem>>()
    val listFollower : LiveData<List<ItemsItem>> = _listFollower

    private val _listFollowing = MutableLiveData<List<ItemsItem>>()
    val listFollowing : LiveData<List<ItemsItem>> = _listFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val followLoading: LiveData<Boolean> = _isLoading


    fun getProfile(q : String ) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUsers(q)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(call: Call<DetailUserResponse>, response: Response<DetailUserResponse>) {
                _isLoading.value = false
                if (response.isSuccessful)
                    _userProfile.value = response.body()

            }
            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

    fun getFollowers(q : String ) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollower(q, "50000")
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listFollower.value = response.body()

                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getFollowing(q : String ) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(q, "50000")
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listFollowing.value = response.body()

                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }


    companion object{
        private const val TAG = "DetailModelView"
    }
}