package com.mahdikaseatashin.shotshot.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mahdikaseatashin.shotshot.R
import com.mahdikaseatashin.shotshot.database.model.UserEntity
import com.mahdikaseatashin.shotshot.databinding.UserItemBinding
import com.mahdikaseatashin.shotshot.utils.UsersDiffCallback
import kotlinx.android.synthetic.main.user_item.view.*


class UserRecyclerViewAdapter(
    private var userList: List<UserEntity>,
    private var selectedUser: UserEntity?
) :
    RecyclerView.Adapter<UserRecyclerViewAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.user_item,
                parent,
                false
            )
        )
    }

    fun updateUserList(newUsersList: List<UserEntity>?) {
        val diffResult = DiffUtil.calculateDiff(UsersDiffCallback(userList, newUsersList), false)
        userList = newUsersList!!
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int = userList.size

    class UserViewHolder(binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var dataBinding = binding
    }


    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.dataBinding.setVariable(BR.user, userList[position])
        if (userList[position] == selectedUser)
            holder.itemView.setBackgroundColor(R.color.white)
        if (userList[position].image != "No Image")
            holder.itemView.iv_user_image.setImageURI(Uri.parse(userList[position].image))
        else
            holder.itemView.iv_user_image.setImageResource(R.drawable.bg_photo_place_holder)
    }

//    override fun getSectionTitle(position: Int): String {
//        return userList[position].instaId.substring(0, 1)
//    }
}
