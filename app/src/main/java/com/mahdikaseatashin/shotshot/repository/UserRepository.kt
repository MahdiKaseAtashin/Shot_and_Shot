package com.mahdikaseatashin.shotshot.repository

import androidx.lifecycle.LiveData
import com.mahdikaseatashin.shotshot.database.AppDatabase
import com.mahdikaseatashin.shotshot.database.dao.UserDAO
import com.mahdikaseatashin.shotshot.database.model.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserRepository(appDatabase: AppDatabase) {
    private var userDAO: UserDAO = appDatabase.getUserDAO()

    fun getUsers(): LiveData<List<UserEntity>> {
        return userDAO.getAllUsers()
    }

    fun getUsersList(): List<UserEntity> {
        return userDAO.getAllUsersList()
    }


    fun getUserByIFG(
        minRate: Int,
        maxRate: Int,
        minFollower: Long,
        maxFollower: Long,
        gender: String
    ): LiveData<List<UserEntity>> {
        return userDAO.getUserByIFG(minRate, maxRate, minFollower, maxFollower, gender, "Both")
    }

    fun getUserByIF(
        minRate: Int,
        maxRate: Int,
        minFollower: Long,
        maxFollower: Long
    ): LiveData<List<UserEntity>> {
        return userDAO.getUserByIF(minRate, maxRate, minFollower, maxFollower)
    }

    fun getUsersByFollowers(): LiveData<List<UserEntity>> {
        return userDAO.getAllUsersSortByFollowers()
    }

    fun getUsersByInteraction(): LiveData<List<UserEntity>> {
        return userDAO.getAllUsersSortByInteraction()
    }

    fun insertUser(userEntity: UserEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            userDAO.addUser(userEntity)
        }
    }

    fun deleteUser(userEntity: UserEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            userDAO.deleteUser(userEntity)
        }
    }

    fun updateUser(userEntity: UserEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            userDAO.updateUser(userEntity)
        }
    }
}
