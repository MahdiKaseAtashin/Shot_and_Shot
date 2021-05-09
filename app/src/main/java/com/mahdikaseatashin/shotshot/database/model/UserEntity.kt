package com.mahdikaseatashin.shotshot.database.model

import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mahdikaseatashin.shotshot.utils.Constants
import com.mahdikaseatashin.shotshot.view.details.DetailsActivity
import com.mahdikaseatashin.shotshot.view.interaction.InteractionActivity
import com.mahdikaseatashin.shotshot.view.main.MainActivity

@Entity(
    tableName = Constants.TABLE_USER,
//    foreignKeys = [ForeignKey(
//        entity = CategoryEntity::class,
//        parentColumns = ["category_id"],
//        childColumns = ["user_category_id"],
//        onDelete = ForeignKey.CASCADE
//    )]
)
data class UserEntity(

    @ColumnInfo(name = "user_id")
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "user_insta_id")
    var instaId: String,

    @ColumnInfo(name = "user_followers_num")
    var followerNum: Long,

    @ColumnInfo(name = "user_followers")
    var follower: Long,

    @ColumnInfo(name = "user_follower_extension")
    var followerEx: String,

    @ColumnInfo(name = "user_gender")
    var gender: String,

    @ColumnInfo(name = "user_interaction_rate")
    var interaction: Int,

//    @ColumnInfo(name = "user_category_id")
//    var categoryID: Long,


    @ColumnInfo(name = "user_image")
    var image: String,

    @ColumnInfo(name = "user_phone")
    var phone: String,

    @ColumnInfo(name = "user_interaction_number")
    var interactionNum: Int

    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(instaId)
        parcel.writeLong(followerNum)
        parcel.writeLong(follower)
        parcel.writeString(followerEx)
        parcel.writeString(gender)
        parcel.writeInt(interaction)
        parcel.writeString(image)
        parcel.writeString(phone)
        parcel.writeInt(interactionNum)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserEntity> {
        override fun createFromParcel(parcel: Parcel): UserEntity {
            return UserEntity(parcel)
        }

        override fun newArray(size: Int): Array<UserEntity?> {
            return arrayOfNulls(size)
        }
    }

    fun getUserFollowerNumber(user: UserEntity): String {
        return when (user.followerEx) {
            "M" -> "${user.followerNum} M"
            "K" -> "${user.followerNum} K"
            else -> "${user.followerNum}"
        }
    }

//     data binding onClick
    fun onUserItemClicked(view: View, userEntity: UserEntity) {
        Log.e("TAG", "onUserItemClicked: ${view.context}" )
        if (view.context::class.java == MainActivity::class.java) {
            val intent = Intent(view.context, InteractionActivity::class.java)
            intent.putExtra("selected_user", userEntity)
            view.context.startActivity(intent)
        }else{
            val intent = Intent(view.context, DetailsActivity::class.java)
            intent.putExtra("selected_user", userEntity)
            view.context.startActivity(intent)
        }
    }


}
