package com.inksy.UI.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.inksy.Model.ChatDataModel
import com.inksy.Model.ChatMainModel
import com.inksy.Remote.APIInterface
import com.inksy.Remote.Resource
import com.inksy.UI.Repositories.ChatRepo


class ChatView : ViewModel() {

    private var mutableLiveDataChat: MutableLiveData<Resource<APIInterface.ApiResponse<List<ChatDataModel>>>>? =
        null
    private var chatRepo: ChatRepo? = null

    fun init() {
        chatRepo = ChatRepo.getInstance()
        if (mutableLiveDataChat != null) {
            return
        }
    }

    fun chatList(
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<List<ChatDataModel>>>>? {
        mutableLiveDataChat = chatRepo!!.chatList(token)
        return mutableLiveDataChat
    }

}