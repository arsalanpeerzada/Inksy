package com.inksy.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class IsFollowed(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("follower_id") var followerId: Int? = null,
    @SerializedName("following_id") var followingId: Int? = null,
    @SerializedName("status") var status: Int? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null
) : Serializable
