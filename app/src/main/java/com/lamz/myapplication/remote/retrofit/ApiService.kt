package com.lamz.myapplication.remote.retrofit


import com.lamz.myapplication.remote.response.DetailUserResponse
import com.lamz.myapplication.remote.response.GithubResponse
import com.lamz.myapplication.remote.response.ItemsItem
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @GET("search/users")
    fun getListUsers(@Query("q") q: String): Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUsers(@Path("username") login : String) : Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollower(@Path("username") followersUrl : String,@Query("per_page") per_page: String) : Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username")followingUrl : String, @Query("per_page") per_page: String) : Call<List<ItemsItem>>

//    @GET("top-headlines?country=id&category=science")
//    fun getUser(@Query("login") login: String): Call<DetailUserResponse>

}