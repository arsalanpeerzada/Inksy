package com.inksy.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.inksy.Database.Entities.*

@Database(
    entities = [JournalIndexTable::class, PageTable::class, PurchasedDoodles::class, CategoryTable::class, SelectedAudience::class, PageTableForLinks::class],
    version = 35,
    exportSchema = false
)
abstract class JournalDatabase : RoomDatabase() {

    abstract fun getJournalData(): iJournalSave

    companion object {
        // Singleton prevents multiple
        // instances of database opening at the
        // same time.

        private var journalDatabase: JournalDatabase? = null

        fun getInstance(context: Context): JournalDatabase? {
            if (null == journalDatabase) {
                journalDatabase = buildDatabaseInstance(context)
            }
            return journalDatabase
        }
        private fun buildDatabaseInstance(context: Context): JournalDatabase {
            return Room.databaseBuilder(
                context,
                JournalDatabase::class.java,
                "JournalDatabase"
            ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
        }
    }

    fun cleanUp() {
        journalDatabase = null
    }
}