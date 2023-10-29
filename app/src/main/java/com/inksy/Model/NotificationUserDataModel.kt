package com.inksy.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NotificationUserDataModel {
    @SerializedName("user_id")
    @Expose
    var userId: Int? = null

    @SerializedName("full_name")
    @Expose
    var fullName: String? = null

    @SerializedName("avatar")
    @Expose
    var avatar: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("noti_type")
    @Expose
    var notiType: String? = null

    @SerializedName("meta")
    @Expose
    var meta: NotificationMetaModel? = null
}