package com.mahdikaseatashin.shotshot.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mahdikaseatashin.shotshot.utils.Constants

@Entity(tableName = Constants.TABLE_CATEGORY)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    var categoryID: Long,
    @ColumnInfo(name = "category_name")
    var categoryName: String,
    @ColumnInfo(name = "category_description")
    var categoryDesc: String
) {

    // for spinner
    override fun toString(): String {
        return categoryName
    }
}
