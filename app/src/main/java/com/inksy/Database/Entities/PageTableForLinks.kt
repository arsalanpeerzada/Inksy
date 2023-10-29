package com.inksy.Database.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pagetableforlinks")
class PageTableForLinks(
    @PrimaryKey(autoGenerate = true) val pageId: Int,
    @ColumnInfo(name ="layoutid") val layoutid : String? = null,
    @ColumnInfo(name = "bulletnumber") val bulletnumber : Int ? = null,
    @ColumnInfo(name = "pageBackground") val pageBackground: String? = null,
    @ColumnInfo(name = "pageTitle") val pageTitle: String? = null,
    @ColumnInfo(name = "arrayOfBullets") val arrayOfBullets: String? = null,
    @ColumnInfo(name = "arrayOfText") val arrayOfText: String? = null,
    @ColumnInfo(name = "arrayOfImage") val arrayOfImage: String? = null,
    @ColumnInfo(name = "journalId") val journalId: String? = null
) {

}