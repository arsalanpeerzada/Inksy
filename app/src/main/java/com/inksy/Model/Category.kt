package com.inksy.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Category(

  @SerializedName("id") var id: Int? = null,
  @SerializedName("category_name") var categoryName: String? = null

) : Serializable