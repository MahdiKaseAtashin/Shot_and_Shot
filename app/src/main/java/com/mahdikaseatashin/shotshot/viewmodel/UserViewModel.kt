package com.mahdikaseatashin.shotshot.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mahdikaseatashin.shotshot.database.model.UserEntity
import com.mahdikaseatashin.shotshot.repository.UserRepository
import com.opencsv.CSVWriter
import java.io.File
import java.io.FileWriter


class UserViewModel(
    private var userRepository: UserRepository
) : ViewModel() {
    var isLoading = MutableLiveData<Boolean>()
    val isUserIdEmpty = MutableLiveData<Boolean>()
    val isUserFollowerEmpty = MutableLiveData<Boolean>()
    val isUserFollowerExEmpty = MutableLiveData<Boolean>()
    val isUserInteractionEmpty = MutableLiveData<Boolean>()
    val isUserGenderEmpty = MutableLiveData<Boolean>()
    val shouldFinishActivity = MutableLiveData<Boolean>()
    var saveImageInInternalStorage: Uri? = null
    var selectedUser: UserEntity? = null
    var isUpdateUser = false
    var instaId: String? = null
    var phone: String? = null
    var followerNumber: String? = null
    var followerEx: String? = null
    var gender: String? = null
    var interaction: String? = null
    var image: String? = null

    fun writeToCsv() {
        val baseDir: String = android.os.Environment.getExternalStorageDirectory().absolutePath
        Log.e("TAG", "writeToCsv: $baseDir")
        val fileName = "AnalysisData.csv"
        val filePath: String = baseDir + File.separator + fileName
        val f = File(filePath)
        // File exist
        val writer: CSVWriter = if (f.exists() && !f.isDirectory) {
            val mFileWriter = FileWriter(filePath, false)
            CSVWriter(mFileWriter)
        } else {
            CSVWriter(FileWriter(filePath))
        }
        val users = getUsersList()
        val dataTemp = arrayOf(
            "ID",
            "Insta ID",
            "Follower",
            "Interaction",
            "Gender",
            "Phone Number",
            "Follower Extension",
            "Follower Number",
            "Interaction Number"
        )
        writer.writeNext(dataTemp)
        for (user in users) {
            val data = arrayOf(
                user.id.toString(),
                user.instaId,
                user.follower.toString(),
                user.interaction.toString(),
                user.gender,
                user.phone,
                user.followerEx,
                user.followerNum.toString(),
                user.interactionNum.toString()
            )
            writer.writeNext(data)
        }
        writer.close()
    }

    fun addUser(user: UserEntity) {
        return userRepository.insertUser(user)
    }


    fun setUserId(charSequence: CharSequence) {
        instaId = charSequence.toString()
    }

    fun setPhone(charSequence: CharSequence) {
        phone = charSequence.toString()
    }

    fun setFollowerNumber(charSequence: CharSequence) {
        followerNumber = charSequence.toString()
    }

    fun setGender(charSequence: CharSequence) {
        gender = charSequence.toString()
    }

    fun setInteraction(charSequence: CharSequence) {
        interaction = charSequence.toString()
    }

    fun setFollowerEx(charSequence: CharSequence) {
        followerEx = charSequence.toString()
    }

    fun addInteraction() {
        selectedUser?.let { user ->
            user.interactionNum++
            update(user)
        }
        shouldFinishActivity.value = true
    }

    fun addInteraction(user: UserEntity) {
        user.interactionNum++
        update(user)
    }

    fun saveUser() {
        Log.e("ViewModel", "saveUser: Saving ")
        isUserIdEmpty.value = instaId.isNullOrEmpty()
        isUserFollowerEmpty.value = followerNumber.isNullOrEmpty()
        isUserFollowerExEmpty.value = followerEx.isNullOrEmpty()
        isUserInteractionEmpty.value = interaction.isNullOrEmpty()
        isUserGenderEmpty.value = gender.isNullOrEmpty()
        if (!instaId.isNullOrEmpty() && !followerNumber.isNullOrEmpty() && !interaction.isNullOrEmpty() && !gender.isNullOrEmpty() && !followerEx.isNullOrEmpty()) {
            Log.e("TAG", "saveUser: 1")
            if (isUpdateUser) {
                Log.e("TAG", "saveUser: 2")
                selectedUser?.let { user ->
                    if (saveImageInInternalStorage != null)
                        image = saveImageInInternalStorage.toString()
                    user.instaId = instaId!!
                    user.followerNum = followerNumber!!.toLong()
                    user.followerEx = followerEx!!
                    user.follower = when (followerEx) {
                        "M" -> user.followerNum * 1_000_000
                        "K" -> user.followerNum * 1_000
                        else -> user.followerNum
                    }
                    user.interaction = interaction!!.toInt()
                    user.gender = gender!!
                    Log.e("TAG", "saveUser: $image")
                    user.image = image!!
                    user.phone = phone!!
                    update(user)
                }
            } else {
                image = if (saveImageInInternalStorage != null)
                    saveImageInInternalStorage.toString()
                else
                    "No Image"
                val follower = when (followerEx) {
                    "M" -> followerNumber!!.toFloat() * 1_000_000
                    "K" -> followerNumber!!.toFloat() * 1_000
                    else -> followerNumber!!.toFloat()
                }

                val newUser = UserEntity(
                    0,
                    instaId!!,
                    followerNumber!!.toLong(),
                    follower.toLong(),
                    followerEx!!,
                    gender!!,
                    interaction!!.toInt(),
                    image!!,
                    phone!!,
                    0
                )
                insert(newUser)
            }
            shouldFinishActivity.value = true
        } else
            shouldFinishActivity.value = false
    }


    private fun update(user: UserEntity) {
        return userRepository.updateUser(user)
    }

    fun getAllUsers(): LiveData<List<UserEntity>> {
        return userRepository.getUsers()
    }

    fun getUsersList(): List<UserEntity> {
        return userRepository.getUsersList()
    }

    fun getUserByIFG(
        minRate: Int,
        maxRate: Int,
        minFollower: Long,
        maxFollower: Long,
        gender: String
    ): LiveData<List<UserEntity>> {
        return userRepository.getUserByIFG(minRate, maxRate, minFollower, maxFollower, gender)
    }

    fun getUserByIF(
        minRate: Int,
        maxRate: Int,
        minFollower: Long,
        maxFollower: Long
    ): LiveData<List<UserEntity>> {
        return userRepository.getUserByIF(minRate, maxRate, minFollower, maxFollower)
    }

    fun getAllUsersSortByFollower(): LiveData<List<UserEntity>> {
        return userRepository.getUsersByFollowers()
    }

    fun getAllUsersSortByInteraction(): LiveData<List<UserEntity>> {
        return userRepository.getUsersByInteraction()
    }

    fun deleteUser(user: UserEntity) {
        return userRepository.deleteUser(user)
    }

    private fun insert(user: UserEntity) {
        return userRepository.insertUser(user)
    }


}
