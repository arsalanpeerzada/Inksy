package com.inksy.Database.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "journalIndex")
class JournalIndexTable(
    @PrimaryKey(autoGenerate = false) val journalId: String,
    @ColumnInfo(name = "jid") val jId: String? = null,
    @ColumnInfo(name = "indexTemplate") val indexTemplate: String? = null,
    @ColumnInfo(name = "indexTemplateid") val indexTemplateid: String? = null,
    @ColumnInfo(name = "indexBackground") val indexBackground: String? = null,
    @ColumnInfo(name = "journalTitle") val journalTitle: String? = null,
    @ColumnInfo(name = "privacy") val privacy: String? = null,
    @ColumnInfo(name = "categoryId") val categoryId: String? = null,
    @ColumnInfo(name = "categoryName") val categoryName: String? = null,
    @ColumnInfo(name = "coverImage") val coverImage: String? = null,
    @ColumnInfo(name = "coverImageString") val coverImageString: String? = null,
    @ColumnInfo(name = "coverDescription") val coverDescription: String? = null,
    @ColumnInfo(name = "coverColor") val coverColor: String? = null,
    @ColumnInfo(name = "arrayOfBullets") val arrayOfBullets: String? = null,
    @ColumnInfo(name = "arrayOfText") val arrayOfText: String? = null,
    @ColumnInfo(name = "arrayOfImage") val arrayOfImage: String? = null,
    @ColumnInfo(name = "numberofPages") val numberofpages: Int? = 0,
    @ColumnInfo(name = "htmlContent") val htmlContent: String? = null,


    ) {
}