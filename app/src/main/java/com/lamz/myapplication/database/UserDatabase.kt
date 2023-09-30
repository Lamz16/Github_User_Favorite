package com.lamz.myapplication.database

import android.content.Context
import androidx.room.*

@Database(entities = [UserEntity::class], version = 1)
abstract class UserDatabase : RoomDatabase() {

    abstract fun favoriteDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): UserDatabase {
            if (INSTANCE == null) {
                synchronized(UserDatabase::class.java) {
                    INSTANCE =
                        Room.databaseBuilder(
                            context.applicationContext,
                            UserDatabase::class.java, "favorite_user_database"
                        )
                            .build()
                }
            }
            return INSTANCE as UserDatabase
        }
    }

}