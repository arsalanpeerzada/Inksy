package com.inksy.Model

import com.google.gson.annotations.SerializedName


data class OthersModel (

  @SerializedName("id"          ) var id          : Int?    = null,
  @SerializedName("title"       ) var title       : String? = null,
  @SerializedName("description" ) var description : String? = null,
  @SerializedName("created_at"  ) var createdAt   : String? = null,
  @SerializedName("updated_at"  ) var updatedAt   : String? = null,
  @SerializedName("slug"        ) var slug        : String? = null,
  @SerializedName("is_active"   ) var isActive    : Int?    = null

)