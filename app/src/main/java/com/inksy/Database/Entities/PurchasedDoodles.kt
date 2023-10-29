package com.inksy.Database.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class PurchasedDoodles(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "doodle_id") val doodle_id: String? = null,
    @ColumnInfo(name = "user_id") val user_id: String? = null,
    @ColumnInfo(name = "doodle_image") val doodle_image: String? = null,
    @ColumnInfo(name = "status") val status: String? = null,
    @ColumnInfo(name = "created_at") val created_at: String? = null,
    @ColumnInfo(name = "updated_at") val updated_at: String? = null,
) {


}