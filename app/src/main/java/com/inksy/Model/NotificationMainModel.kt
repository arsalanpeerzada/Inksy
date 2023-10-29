package com.inksy.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NotificationMainModel {
    @SerializedName("success")
    @Expose
    var success: Int? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<NotificationDataModel>? = null
}