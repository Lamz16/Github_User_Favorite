package com.lamz.myapplication.database


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update


@Dao
interface UserDao {
    @Query("SELECT * FROM myfavorite ORDER BY username ASC")
    fun getUser(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM myfavorite where favorite = 1")
    fun getFavoriteUser(): LiveData<List<UserEntity>>

    @PrimaryKey(autoGenerate = false)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteUser : List<UserEntity>)

    @Query("DELETE FROM MyFavorite WHERE favorite = 0")
    fun delete()

    @Update
    fun updateFavorite(favoriteUser: UserEntity)

    @Query("SELECT * FROM MyFavorite ORDER BY username ASC")
    fun getAllFavoriteUsers(): LiveData<List<UserEntity>>

    @Query("SELECT EXISTS(SELECT * FROM MyFavorite WHERE username = :title AND favorite = 1)")
    fun isUserFavorite(title: String): Boolean

}