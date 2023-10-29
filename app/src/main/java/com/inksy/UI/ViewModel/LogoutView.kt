package com.inksy.UI.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonElement
import com.inksy.Remote.APIInterface
import com.inksy.UI.Repositories.LogoutRepo


class LogoutView : ViewModel() {

    private var mutableLiveData: MutableLiveData<APIInterface.ApiResponse<JsonElement>>? = null
    private var logoutRepo: LogoutRepo? = null

    fun init() {
        logoutRepo = LogoutRepo.getInstance()
        if (mutableLiveData != null) {
            return
        }


    }

    fun logout(token: String?): LiveData<APIInterface.ApiResponse<JsonElement>?>? {
        mutableLiveData = logoutRepo!!.logout(token)
        return mutableLiveData
    }
}