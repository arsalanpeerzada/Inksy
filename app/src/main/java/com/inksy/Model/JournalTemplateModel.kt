package com.inksy.Model

import com.google.gson.annotations.SerializedName

data class JournalTemplateModel(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("template_image") var templateImage: String? = null,
    @SerializedName("created_by") var createdBy: Int? = null,
    @SerializedName("is_admin") var isAdmin: Int? = null,
    @SerializedName("is_active") var isActive: Int? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null
)
