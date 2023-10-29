package com.inksy.Model

import com.google.gson.annotations.SerializedName

class CreateDoodleModel(
    @SerializedName("pack_title") var packTitle: String? = null,
    @SerializedName("cover_image") var coverImage: String? = null,
    @SerializedName("price") var price: String? = null,
    @SerializedName("created_by") var createdBy: Int? = null,
    @SerializedName("is_active") var isActive: Int? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("id") var id: Int? = null
)