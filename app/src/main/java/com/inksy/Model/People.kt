package com.inksy.Model

import com.google.gson.annotations.SerializedName


data class People (

  @SerializedName("id"        ) var id        : Int?                 = null,
  @SerializedName("full_name" ) var fullName  : String?              = null,
  @SerializedName("avatar"    ) var avatar    : String?              = null,
  @SerializedName("followings" ) var followers : ArrayList<UserModel> = arrayListOf()

)