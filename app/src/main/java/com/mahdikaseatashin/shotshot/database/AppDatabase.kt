package com.mahdikaseatashin.shotshot.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mahdikaseatashin.shotshot.database.dao.CategoryDAO
import com.mahdikaseatashin.shotshot.database.dao.UserDAO
import com.mahdikaseatashin.shotshot.database.model.CategoryEntity
import com.mahdikaseatashin.shotshot.database.model.UserEntity
import com.mahdikaseatashin.shotshot.utils.Constants

@Database(entities = [UserEntity::class, CategoryEntity::class], version = Constants.DB_VERSION)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getCategoryDAO(): CategoryDAO
    abstract fun getUserDAO(): UserDAO
}
