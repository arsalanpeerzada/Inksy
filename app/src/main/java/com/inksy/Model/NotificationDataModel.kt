package com.inksy.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NotificationDataModel {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("notifiable_type")
    @Expose
    var notifiableType: String? = null

    @SerializedName("notifiable_id")
    @Expose
    var notifiableId: Int? = null

    @SerializedName("data")
    @Expose
    var data: NotificationUserDataModel? = null

    @SerializedName("read_at")
    @Expose
    var readAt: Any? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
}