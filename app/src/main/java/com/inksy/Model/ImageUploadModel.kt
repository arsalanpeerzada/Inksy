package com.inksy.Model

import com.google.gson.annotations.SerializedName

data class ImageUploadModel(
    @SerializedName("avatar") var avatar: String? = null,
)
