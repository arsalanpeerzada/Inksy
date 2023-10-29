package com.inksy.UI.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.inksy.Model.DashboardDataModel
import com.inksy.Model.Journals
import com.inksy.Model.UserModel
import com.inksy.Remote.APIInterface
import com.inksy.Remote.Resource
import com.inksy.UI.Repositories.DashboardRepo


class DashboardView : ViewModel() {

    private var mutableLiveData: MutableLiveData<Resource<APIInterface.ApiResponse<DashboardDataModel>>>? = null
    private var mutableLiveDataPeople: MutableLiveData<Resource<APIInterface.ApiResponse<List<UserModel>>>>? = null
    private var dashboardRepo: DashboardRepo? = null

    private var mutableLiveDataJournal: MutableLiveData<Resource<APIInterface.ApiResponse<List<Journals>>>>? = null


    fun init() {
        dashboardRepo = DashboardRepo.getInstance()
        if (mutableLiveData != null) {
            return
        }
    }

    fun getData(token : String): MutableLiveData<Resource<APIInterface.ApiResponse<DashboardDataModel>>>? {
        mutableLiveData = dashboardRepo!!.getData(token)
        return mutableLiveData
    }

    fun searchUser(searchUser : String , token : String): MutableLiveData<Resource<APIInterface.ApiResponse<List<UserModel>>>>? {
        mutableLiveDataPeople = dashboardRepo!!.searchUser(searchUser,token)
        return mutableLiveDataPeople
    }

    fun searchJournal(searchUser : String , token : String): MutableLiveData<Resource<APIInterface.ApiResponse<List<Journals>>>>? {
        mutableLiveDataJournal = dashboardRepo!!.searchJournal(searchUser,token)
        return mutableLiveDataJournal
    }

    fun searchCategory(searchUser : String , token : String): MutableLiveData<Resource<APIInterface.ApiResponse<List<Journals>>>>? {
        mutableLiveDataJournal = dashboardRepo!!.searchCategory(searchUser,token)
        return mutableLiveDataJournal
    }
}