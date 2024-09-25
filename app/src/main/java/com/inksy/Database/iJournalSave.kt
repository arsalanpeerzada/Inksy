package com.inksy.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inksy.Database.Entities.*

@Dao
interface iJournalSave {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJournalIndexTable(journals: JournalIndexTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDoodles(purchasedDoodles: PurchasedDoodles)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPage(pagetable: PageTable)

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertPagetableforlinks(pagetableforlinks: PageTableForLinks)

//    @Query("Insert into PageTable values(:pageId,:backgroundImage,:pagetitle,:bulletArray,:textArray,:imageArray,:journalId)")
//    fun insertIntoPageTable(
//        pageId: Int,
//        backgroundImage: String,
//        pagetitle: String,
//        bulletArray: String,
//        textArray: String,
//        imageArray: String,
//        journalId: String
//    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(categoryTable: CategoryTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAudeience(audience: SelectedAudience)

    @Query("Insert into journalIndex(indexBackground,journalId) values(:indexBackground,:id)")
    fun createJournalWithIndexBackGround(indexBackground: String, id: String)


    @Query("Select * from PageTableForLinks order by pageId ASC")
    fun getAllLinks(): List<PageTableForLinks>

    @Query("Select * from SelectedAudience order by audienceId ASC")
    fun getAllAudience(): List<SelectedAudience>

    @Query("Select * from CategoryTable order by categoryId ASC")
    fun getAllCategories(): List<CategoryTable>

    @Query("Select * from journalIndex order by journalId ASC")
    fun getAllNotes(): List<JournalIndexTable>

    @Query("Select * from pagetable order by pageId ASC")
    fun getAllPages(): List<PageTable>

    @Query("Select * from PurchasedDoodles order by id ASC")
    fun getAllDoodles(): List<PurchasedDoodles>

    @Query("Select * from pagetable where pageId = :pageid")
    fun getOnePage(pageid: String): PageTable


    @Query("Update journalIndex SET privacy = :privacy where journalId = :id")
    fun insertprivacy(privacy: String, id: String)


    @Query("Update journalIndex SET indexTemplate = :indexTemplate where  journalId = :id ")
    fun UpdateJournalWithIndexTemplate(indexTemplate: String, id: String)

    @Query("Update journalIndex SET indexBackground = :indexBackground where  journalId = :id ")
    fun UpdateJournalWithIndexBackGround(indexBackground: String, id: String)

    @Query("Update journalIndex SET coverImageString = :cover where journalId = :id")
    fun UpdateCoverImage(cover: String, id: String)

    @Query("Update journalIndex SET indexBackground = :indexBackground where journalId = :id")
    fun UpdateIndexCover(indexBackground: String, id: String)

    @Query("Update journalIndex SET privacy = :privacy where journalId = :id")
    fun updateprivacy(privacy: String, id: String)

    @Query("Update journalIndex SET  coverColor = :qcoverColor where journalId = :id  ")
    fun insertCoverColor(qcoverColor: String, id: String)

    @Query("Update pagetable SET pageBackground = :cover where pageId = :id")
    fun UpdatePageCover(cover: String, id: String)


    @Query("Update journalIndex SET journalTitle  = :qjournalTitle,coverDescription = :qcoverDescription,coverImage = :qcoverImage,categoryId = :qcategoryId,categoryName = :qcategoryName where journalId = :qjournalId ")
    fun insertCover(
        qjournalId: String,
        qjournalTitle: String,
        qcoverDescription: String,
        qcoverImage: String,
        qcategoryId: String,
        qcategoryName: String
    )

    @Query("Update journalIndex SET arrayOfBullets  = :arrayOfBullets,arrayOfText = :arrayOfText,arrayOfImage = :arrayOfImages where journalId = :qjournalId ")
    fun insertData(
        qjournalId: String,
        arrayOfBullets: String,
        arrayOfText: String,
        arrayOfImages: String,
    )


    @Query("Delete from journalIndex where journalId = :qjournalId ")
    fun DeleteData(
        qjournalId: String,
    )

    @Query("Delete from SelectedAudience where userID = :userID ")
    fun DeleteAudience(
        userID: String,
    )

    @Query("DELETE FROM pagetable")
    fun deleteTable()

    @Query("DELETE FROM pagetableforlinks")
    fun deletepageforlinkTable()

    @Query("DELETE FROM SelectedAudience")
    fun deleteSelectedAudience()

    @Query("DELETE FROM CategoryTable")
    fun deleteCategoryTable()

    @Query("DELETE FROM PurchasedDoodles")
    fun deletePurchasedDoodle()


}