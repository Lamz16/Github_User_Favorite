package com.lamz.myapplication.model


import androidx.lifecycle.ViewModel
import com.lamz.myapplication.data.UserRepository
import com.lamz.myapplication.database.UserEntity

class MainViewModel(private val userRepository: UserRepository): ViewModel(){
    fun saveFavorite(favorite : UserEntity) {
        userRepository.setFavoriteUser(favorite,true)
    }
    fun deleteFavorite(favorite: UserEntity) {
        userRepository.setFavoriteUser(favorite, false)
    }

    val Loading = userRepository.isLoading
    fun getAllUser(q: String= "") = userRepository.getAllUser(q)

    fun getFavoriteUser() = userRepository.getAllFavoriteUser()


}