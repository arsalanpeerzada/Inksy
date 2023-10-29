package com.inksy.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ChatDataModel {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("chat_id")
    @Expose
    var chatId: String? = null

    @SerializedName("sender_id")
    @Expose
    var senderId: Int? = null

    @SerializedName("receiver_id")
    @Expose
    var receiverId: Int? = null

    @SerializedName("user")
    @Expose
    var user: ChatUserModel? = null
}