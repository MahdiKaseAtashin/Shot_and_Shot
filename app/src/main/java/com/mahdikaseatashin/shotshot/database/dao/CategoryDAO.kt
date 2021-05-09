package com.mahdikaseatashin.shotshot.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mahdikaseatashin.shotshot.database.model.CategoryEntity
import com.mahdikaseatashin.shotshot.utils.Constants

@Dao
interface CategoryDAO {
    @Insert
    fun addCategory(categoryEntity: CategoryEntity): Long

    @Update
    fun updateCategory(categoryEntity: CategoryEntity)

    @Delete
    fun deleteCategory(categoryEntity: CategoryEntity)

    @Query("SELECT * FROM ${Constants.TABLE_CATEGORY}")
    fun getAllCategories(): LiveData<List<CategoryEntity>>

    @Query("SELECT * FROM ${Constants.TABLE_CATEGORY} WHERE category_id == :categoryID")
    fun getCategory(categoryID: Long): CategoryEntity
}
