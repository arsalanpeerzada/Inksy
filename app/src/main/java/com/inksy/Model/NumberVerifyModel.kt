package com.inksy.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NumberVerifyModel(
    @SerializedName("email") var email: String? = null,
    @SerializedName("is_data_exist") var isDataExist: Int? = null
) : Serializable
