package com.inksy.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ChatUserModel {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("full_name")
    @Expose
    var fullName: String? = null

    @SerializedName("avatar")
    @Expose
    var avatar: String? = null

    @SerializedName("device_type")
    @Expose
    var deviceType: String? = null

    @SerializedName("device_token")
    @Expose
    var deviceToken: String? = null
}