package com.inksy.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Notes(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("doodle_pack_id") var doodlePackId: Int? = null,
    @SerializedName("doodle_id") var doodleId: Int? = null,
    @SerializedName("note") var note: String? = null,
    @SerializedName("created_by") var createdBy: Int? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("is_admin") var isAdmin: Int? = null
) : Serializable
