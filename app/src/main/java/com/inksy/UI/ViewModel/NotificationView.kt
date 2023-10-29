package com.inksy.UI.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonElement
import com.inksy.Model.NotificationDataModel
import com.inksy.Model.NumberVerifyModel
import com.inksy.Model.UserModel
import com.inksy.Remote.APIInterface
import com.inksy.Remote.Resource
import com.inksy.UI.Repositories.NotificationRepo
import com.inksy.UI.Repositories.PeopleRepo


class NotificationView : ViewModel() {

    private var mutableLiveDataNotification: MutableLiveData<Resource<APIInterface.ApiResponse<List<NotificationDataModel>>>>? =
        null
    private var mutableLiveDataSendMessageNotification: MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? =
        null
    private var mutableLiveDataCreateChatNode: MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? =
        null
    private var mutableLiveDataClearNotifications: MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? =
        null
    private var notificationRepo: NotificationRepo? = null

    fun init() {
        notificationRepo = NotificationRepo.getInstance()
        if (mutableLiveDataNotification != null) {
            return
        }
    }

    fun notificationsList(
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<List<NotificationDataModel>>>>? {
        mutableLiveDataNotification = notificationRepo!!.notificationsList(token)
        return mutableLiveDataNotification
    }

    fun sendMessageNotification(
        receiverId: Int, token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? {
        mutableLiveDataSendMessageNotification = notificationRepo!!.sendMessageNotification(receiverId, token)
        return mutableLiveDataSendMessageNotification
    }

    fun createChatNode(
        receiverId: Int, token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? {
        mutableLiveDataCreateChatNode = notificationRepo!!.createChatNode(receiverId, token)
        return mutableLiveDataCreateChatNode
    }

    fun clearNotifications(
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? {
        mutableLiveDataClearNotifications = notificationRepo!!.clearNotifications(token)
        return mutableLiveDataClearNotifications
    }

}