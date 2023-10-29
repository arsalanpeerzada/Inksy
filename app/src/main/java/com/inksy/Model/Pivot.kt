package com.inksy.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Pivot (

  @SerializedName("following_id" ) var followingId : Int? = null,
  @SerializedName("follower_id"  ) var followerId  : Int? = null,
  @SerializedName("status"       ) var status      : Int? = null

) : Serializable