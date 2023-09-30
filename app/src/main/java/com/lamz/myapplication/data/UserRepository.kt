package com.lamz.myapplication.data


import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.lamz.myapplication.database.UserDao
import com.lamz.myapplication.database.UserEntity
import com.lamz.myapplication.remote.response.DetailUserResponse
import com.lamz.myapplication.remote.response.GithubResponse
import com.lamz.myapplication.remote.retrofit.ApiConfig
import com.lamz.myapplication.remote.retrofit.ApiService
import com.lamz.myapplication.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(
    private val apiService: ApiService,
    private val favoriteDao : UserDao,
    private val appExecutors: AppExecutors
    ) {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

private val result = MediatorLiveData<Result<List<UserEntity>>>()


    fun getAllUser(q : String ): LiveData<Result<List<UserEntity>>>{
        _isLoading.value = true
        result.value = Result.Loading
        val client = apiService.getListUsers(q)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(call: Call<GithubResponse>, response: Response<GithubResponse>) {
                _isLoading.value = false
                result.value = Result.Loading
                if (response.isSuccessful) {
                    val items = response.body()?.items
                    val favoriteList = ArrayList<UserEntity>()
                    appExecutors.diskIO.execute {
                        items?.forEach { itemsItem ->
                            val isFavorite = favoriteDao.isUserFavorite(itemsItem.login)
                            val favorite = UserEntity(
                                itemsItem.login,
                                itemsItem.avatarUrl,
                                isFavorite
                            )
                            favoriteList.add(favorite)
                        }
                        favoriteDao.delete()
                        favoriteDao.insert(favoriteList)
                    }
                }
            }
            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })
        val locaData = favoriteDao.getUser()
        result.addSource(locaData){newData :List<UserEntity> ->
            result.value = Result.Success(newData)
        }
        return result
    }

    fun getAllFavoriteUser(): LiveData<List<UserEntity>> {
        return favoriteDao.getFavoriteUser()
    }
    fun setFavoriteUser(favorite: UserEntity, favoriteState: Boolean) {
        appExecutors.diskIO.execute {
            favorite.isFavorite = favoriteState
            favoriteDao.updateFavorite(favorite)
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            favoriteDao: UserDao,
            appExecutors: AppExecutors
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService,favoriteDao, appExecutors)
            }.also { instance = it }
    }


}