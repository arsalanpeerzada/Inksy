package com.inksy.Database.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SelectedAudience")
class SelectedAudience(
    @PrimaryKey(autoGenerate = true) val audienceId: Int,
    @ColumnInfo(name = "UserId") val userID: String? = null,
    @ColumnInfo(name = "Avatar") val avatar: String? = null,
    @ColumnInfo(name = "bio") val bio: String? = null,
    @ColumnInfo(name = "isArtist") val isArtist: String? = null,
) {

}