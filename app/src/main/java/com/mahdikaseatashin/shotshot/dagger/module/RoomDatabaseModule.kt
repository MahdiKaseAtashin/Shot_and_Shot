package com.mahdikaseatashin.shotshot.dagger.module

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mahdikaseatashin.shotshot.database.AppDatabase
import com.mahdikaseatashin.shotshot.database.model.CategoryEntity
import com.mahdikaseatashin.shotshot.database.model.UserEntity
import com.mahdikaseatashin.shotshot.utils.Constants
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
class RoomDatabaseModule(application: Application) {

    private var app = application

    companion object {
        lateinit var appDatabase: AppDatabase
    }


    private val databaseCallback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Log.e("RoomDatabaseModule", "onCreate")
//            CoroutineScope(Dispatchers.IO).launch {
//                addSampleUserToDatabase()
//            }
        }
    }

//    private fun addSampleUserToDatabase() {
//        val category1 = CategoryEntity(
//            EDUCATIONAL_BOOKS_CATEGORY_ID,
//            "Educational Books",
//            "Educational Books Desc"
//        )
//        val category2 = CategoryEntity(NOVELS_CATEGORY_ID, "Novels", "Novels Desc")
//        val category3 =
//            CategoryEntity(OTHER_BOOKS_CATEGORY_ID, "Other Books", "Non Categorized Books")
//
//        val book1 =
//            UserEntity(1, "Mahdi", 10, "male", 12f, EDUCATIONAL_BOOKS_CATEGORY_ID, "")
//        val book2 =
//            UserEntity(2, "Mathematics", 19, "male", 55f, EDUCATIONAL_BOOKS_CATEGORY_ID, "")
//        val book3 =
//            UserEntity(3, "Adventures of Joe Finn", 25, "male", 55f, NOVELS_CATEGORY_ID, "")
//        val book4 =
//            UserEntity(4, "The Hound the New York", 5, "male", 55f, NOVELS_CATEGORY_ID, "")
//        val book5 = UserEntity(5, "Astrology", 56, "male", 55f, OTHER_BOOKS_CATEGORY_ID, "")
//        val book6 =
//            UserEntity(6, "Arc of Witches", 34, "male", 55.5f, OTHER_BOOKS_CATEGORY_ID, "")
//        val book7 = UserEntity(7, "Can I Run?", 99, "male", 55.24f, NOVELS_CATEGORY_ID, "")
//        val book8 = UserEntity(
//            8,
//            "Basic of Physics",
//            10,
//            "male",
//            35.4f,
//            EDUCATIONAL_BOOKS_CATEGORY_ID,
//            ""
//        )
//
//        val categoryDAO = appDatabase.getCategoryDAO()
//        categoryDAO.addCategory(category1)
//        categoryDAO.addCategory(category2)
//        categoryDAO.addCategory(category3)
//
//        val userDAO = appDatabase.getUserDAO()
//        userDAO.addUser(book1)
//        userDAO.addUser(book2)
//        userDAO.addUser(book3)
//        userDAO.addUser(book4)
//        userDAO.addUser(book5)
//        userDAO.addUser(book6)
//        userDAO.addUser(book7)
//        userDAO.addUser(book8)
//    }

    @Singleton
    @Provides
    fun providesRoomDatabase(): AppDatabase {
        appDatabase = Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            Constants.DB_NAME
        )
            .fallbackToDestructiveMigration()
            .addCallback(databaseCallback)
            .build()
        return appDatabase
    }

    @Singleton
    @Provides
    fun providesCategoryDAO(appDatabase: AppDatabase) = appDatabase.getCategoryDAO()

    @Singleton
    @Provides
    fun providesBookDAO(appDatabase: AppDatabase) = appDatabase.getUserDAO()

}
