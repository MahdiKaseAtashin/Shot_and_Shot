package com.mahdikaseatashin.shotshot.repository

import androidx.lifecycle.LiveData
import com.mahdikaseatashin.shotshot.database.AppDatabase
import com.mahdikaseatashin.shotshot.database.dao.CategoryDAO
import com.mahdikaseatashin.shotshot.database.model.CategoryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryRepository(appDatabase: AppDatabase) {

    private var categoryDAO: CategoryDAO = appDatabase.getCategoryDAO()

    fun getCategories(): LiveData<List<CategoryEntity>> {
        return categoryDAO.getAllCategories()
    }

    fun insertCategory(categoryEntity: CategoryEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            categoryDAO.addCategory(categoryEntity)
        }
    }

    fun deleteCategory(categoryEntity: CategoryEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            categoryDAO.deleteCategory(categoryEntity)
        }
    }

    fun updateCategory(categoryEntity: CategoryEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            categoryDAO.updateCategory(categoryEntity)
        }
    }
}
