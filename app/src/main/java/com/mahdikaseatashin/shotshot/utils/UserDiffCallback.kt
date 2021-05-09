package com.mahdikaseatashin.shotshot.utils

import androidx.recyclerview.widget.DiffUtil
import com.mahdikaseatashin.shotshot.database.model.UserEntity

class UsersDiffCallback(
    private val oldUsersList: List<UserEntity>?,
    private val newUsersList: List<UserEntity>?
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldUsersList?.get(oldItemPosition)?.id == newUsersList?.get(newItemPosition)?.id
    }

    override fun getOldListSize(): Int {
        return oldUsersList?.size ?: 0
    }

    override fun getNewListSize(): Int {
        return newUsersList?.size ?: 0
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldUsersList?.get(oldItemPosition)?.equals(newUsersList?.get(newItemPosition))!!
    }
}
