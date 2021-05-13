package com.mahdikaseatashin.shotshot.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mahdikaseatashin.shotshot.database.model.UserEntity
import com.mahdikaseatashin.shotshot.utils.Constants

@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUser(userEntity: UserEntity): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateUser(userEntity: UserEntity)

    @Delete
    fun deleteUser(userEntity: UserEntity)

    @Query("delete from ${Constants.TABLE_USER}")
    fun deleteAllUsers()

    @Query("SELECT * FROM ${Constants.TABLE_USER} ORDER BY user_insta_id COLLATE NOCASE ASC")
    fun getAllUsers(): LiveData<List<UserEntity>>

@Query("SELECT * FROM ${Constants.TABLE_USER} ORDER BY user_insta_id COLLATE NOCASE ASC")
    fun getAllUsersList(): List<UserEntity>

    @Query("SELECT * FROM ${Constants.TABLE_USER} ORDER BY user_followers COLLATE NOCASE DESC")
    fun getAllUsersSortByFollowers(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM ${Constants.TABLE_USER} ORDER BY user_interaction_rate COLLATE NOCASE DESC")
    fun getAllUsersSortByInteraction(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM ${Constants.TABLE_USER} where user_interaction_rate > :min and user_interaction_rate < :max ORDER BY user_interaction_rate COLLATE NOCASE DESC")
    fun getUsersByInteraction(min : Int,max :Int): LiveData<List<UserEntity>>

    @Query("SELECT * FROM ${Constants.TABLE_USER} where user_interaction_rate > :minRate and user_interaction_rate < :maxRate and user_followers > :minFollower and user_followers < :maxFollower ORDER BY user_followers COLLATE NOCASE DESC")
    fun getUserByIF(minRate : Int,maxRate :Int,minFollower : Long, maxFollower: Long): LiveData<List<UserEntity>>

    @Query("SELECT * FROM ${Constants.TABLE_USER} where user_interaction_rate > :minRate and user_interaction_rate < :maxRate and user_followers > :minFollower and user_followers < :maxFollower and (user_gender = :gender or user_gender = :both) ORDER BY user_followers COLLATE NOCASE DESC")
    fun getUserByIFG(minRate : Int,maxRate :Int,minFollower : Long, maxFollower: Long , gender : String, both : String): LiveData<List<UserEntity>>



}
