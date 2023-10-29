package com.inksy.UI.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonElement
import com.inksy.Database.Entities.PageTable
import com.inksy.Database.Entities.SelectedAudience
import com.inksy.Model.*
import com.inksy.Remote.APIInterface
import com.inksy.Remote.Resource
import com.inksy.UI.Repositories.JournalRepo
import java.io.File


class JournalView : ViewModel() {

    private var mutableLiveData: MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? =
        null
    private var mutableLiveDataJournals: MutableLiveData<Resource<APIInterface.ApiResponse<Journals>>>? =
        null
    private var mutableLiveDataCommentsAll: MutableLiveData<Resource<APIInterface.ApiResponse<List<CommentsDataParent>>>>? =
        null

    private var mutableLiveDataCommentSend: MutableLiveData<Resource<APIInterface.ApiResponse<SendCommentData>>>? =
        null

    private var mutableLiveDataTemplate: MutableLiveData<Resource<APIInterface.ApiResponse<List<JournalTemplateModel>>>>? =
        null

    private var mutableLiveImage: MutableLiveData<Resource<APIInterface.ApiResponse<ImageUploadModel>>>? =
        null

    private var mutableLiveDataCategories: MutableLiveData<Resource<APIInterface.ApiResponse<List<Categories>>>>? =
        null

    private var journalRepo: JournalRepo? = null


//    val allNotes: LiveData<List<JournalIndexTable>>
//    val repository: JournalSaveRepository

//    init {
//        val dao = JournalDatabase.getInstance(application)?.getJournalData()
//        repository = JournalSaveRepository(dao!!)
//        allNotes = repository.allNotes
//    }

    fun init() {

        journalRepo = JournalRepo.getInstance()
        if (mutableLiveData != null) {
            return
        }
    }

    //Database Queries

//    fun addJournalIndex(journalIndexTable: JournalIndexTable) =
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.insert(journalIndexTable)
//        }
//
//    fun addcover(coverImage: String, journalId: String) =
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.createJournal(coverImage, journalId)
//        }
//
//    fun insertCover(coverColor: String, journalId: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.insertcover(coverColor, journalId)
//        }
//    }

//    fun insertPage(pageTable: PageTable) {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.insertPage(pageTable)
//        }
//    }

//    fun insertCoverDetails(
//        journalId: String,
//        journalTitle: String,
//        coverDescription: String,
//        coverImage: String,
//        categoryId: String,
//        categoryName: String
//    ) = viewModelScope.launch(Dispatchers.IO) {
//        repository.insertCoverdetails(
//            journalId,
//            journalTitle,
//            coverDescription,
//            coverImage,
//            categoryId,
//            categoryName
//        )
//    }
//
//    fun insertIndexData(
//        qjournalId: String,
//        arrayOfBullets: String,
//        arrayOfText: String,
//        arrayOfImages: String,
//    ) = viewModelScope.launch(Dispatchers.IO) {
//        repository.insertIndexData(qjournalId, arrayOfBullets, arrayOfText, arrayOfText)
//    }
//
//
//    fun deleteData(id: String) {
//        repository.deleteIndex(id)
//    }


    // ======================================================================================================
    // Rest APIs

    fun journalLike(
        journal_Id: String?,
        token: String?
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? {
        mutableLiveData = journalRepo!!.journalLike(journal_ID = journal_Id!!, token = token!!)
        return mutableLiveData
    }

    fun askforActivation(
        journal_Id: Int?,
        token: String?
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? {
        mutableLiveData = journalRepo!!.askForActivation(journal_ID = journal_Id!!, token = token!!)
        return mutableLiveData
    }

    fun journalDelete(
        journal_Id: String?,
        token: String?
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? {
        mutableLiveData = journalRepo!!.journalDelete(journal_ID = journal_Id!!, token = token!!)
        return mutableLiveData
    }

    fun journalFollow(
        journal_Id: String?,
        token: String?
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? {
        mutableLiveData = journalRepo!!.journalFollow(journal_ID = journal_Id!!, token = token!!)
        return mutableLiveData
    }

    fun journalDetails(
        journal_Id: Int?,
        token: String?
    ): MutableLiveData<Resource<APIInterface.ApiResponse<Journals>>>? {
        mutableLiveDataJournals =
            journalRepo!!.journalDetails(journal_ID = journal_Id!!, token = token!!)
        return mutableLiveDataJournals
    }

    fun journalCreate(
        _token: String,
        _category_id: Int,
        _title: String,
        _cover_bc: String,
        _description: String,
        _html_content: String,
        _protection: String,
        _is_active: String,
        _cover_image: File,
        _page_Image: File,
        list: List<SelectedAudience>,
        pages: List<PageTable>
    ): MutableLiveData<Resource<APIInterface.ApiResponse<Journals>>>? {
        mutableLiveDataJournals = journalRepo!!.journalCreate(
            _token,
            _category_id,
            _title,
            _cover_bc,
            _description,
            _html_content,
            _protection,
            _is_active,
            _cover_image,
            _page_Image,
            list,
            pages,
        )
        return mutableLiveDataJournals
    }

    fun journalUpdate(
        _token: String,
        _category_id: Int,
        _title: String,
        _cover_bc: String,
        _description: String,
        _html_content: String,
        _protection: String,
        _is_active: String,
        _cover_image: File,
        dataImageString: String?,
        dataCoverImage: String?,
        _page_Image: File,
        indexTemplateid: String,
        list: List<SelectedAudience>,
        journalId: String,
        pages: List<PageTable>,
        oldnumberofPages: Int,
    ): MutableLiveData<Resource<APIInterface.ApiResponse<Journals>>>? {
        mutableLiveDataJournals = journalRepo!!.journalUpdate(
            _token,
            _category_id,
            _title,
            _cover_bc,
            _description,
            _html_content,
            _protection,
            _is_active,
            _cover_image,
            dataImageString,
            dataCoverImage,
            _page_Image,
            indexTemplateid,
            list,
            journalId,
            pages,
            oldnumberofPages
        )
        return mutableLiveDataJournals
    }


    fun imageUpload(
        _token: String,
        _cover_image: File
    ): MutableLiveData<Resource<APIInterface.ApiResponse<ImageUploadModel>>>? {
        mutableLiveImage = journalRepo!!.imageUpload(
            _token,
            _cover_image
        )
        return mutableLiveImage
    }

    fun journalReport(
        journal_Id: Int,
        token: String,
        title: String,
        description: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? {
        mutableLiveData = journalRepo!!.reportJournal(
            journal_ID = journal_Id,
            journalTitle = title,
            journalDescription = description,
            token = token!!
        )
        return mutableLiveData
    }


    // ------------------------------------------------------------------------------------------------
    // Comments


    fun getAllComments(
        comments_Id: Int,
        token: String?
    ): MutableLiveData<Resource<APIInterface.ApiResponse<List<CommentsDataParent>>>>? {
        mutableLiveDataCommentsAll = journalRepo!!.getAllComments(comments_Id, token!!)
        return mutableLiveDataCommentsAll
    }

    fun getTemplate(
        token: String?
    ): MutableLiveData<Resource<APIInterface.ApiResponse<List<JournalTemplateModel>>>>? {
        mutableLiveDataTemplate = journalRepo!!.getTemplate(token!!)
        return mutableLiveDataTemplate
    }

    fun getCategoriesList(
        token: String?
    ): MutableLiveData<Resource<APIInterface.ApiResponse<List<Categories>>>>? {
        mutableLiveDataCategories = journalRepo!!.getCategoriesList(token!!)
        return mutableLiveDataCategories
    }

    fun commentLike(
        comment_Id: Int?,
        token: String?
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? {
        mutableLiveData = journalRepo!!.commentLike(comment_ID = comment_Id!!, token = token!!)
        return mutableLiveData
    }

    fun commentDelete(
        comment_Id: Int?,
        token: String?
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? {
        mutableLiveData = journalRepo!!.commenteDelete(comment_ID = comment_Id!!, token = token!!)
        return mutableLiveData
    }

    fun sendComment(
        journalId: Int,
        comment: String,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<SendCommentData>>>? {
        mutableLiveDataCommentSend = journalRepo!!.sendComment(journalId, comment, token)
        return mutableLiveDataCommentSend
    }

    fun sendComment(
        journalId: Int,
        comment: String,
        comment_Id: String,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<SendCommentData>>>? {
        mutableLiveDataCommentSend =
            journalRepo!!.sendComment(journalId, comment, comment_Id, token)
        return mutableLiveDataCommentSend
    }

    fun commentReport(
        comment_Id: Int,
        token: String,
        title: String,
        description: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? {
        mutableLiveData = journalRepo!!.reportComment(
            title,
            description,
            comment_Id,
            token!!
        )
        return mutableLiveData
    }

    fun commentUpdate(
        journal_Id: Int,
        comment_Id: Int,
        comment: String,
        token: String,
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? {
        mutableLiveData = journalRepo!!.commentUpdate(
            journal_Id,
            comment_Id,
            comment,
            token
        )
        return mutableLiveData
    }

}