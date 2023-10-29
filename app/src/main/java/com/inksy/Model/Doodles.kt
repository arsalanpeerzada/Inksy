package com.example.example

import com.google.gson.annotations.SerializedName
import com.inksy.Model.Notes
import java.io.Serializable


data class Doodles(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("doodle_pack_id") var doodlePackId: Int? = null,
    @SerializedName("doodle_image") var doodleImage: String? = null,
    @SerializedName("is_cover") var isCover: Int? = null,
    @SerializedName("is_active") var isActive: Int? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("notes") var notes: ArrayList<Notes> = arrayListOf(),
    @SerializedName("doodle_id") var doodleId: Int? = null,
    @SerializedName("user_id") var userId: Int? = null,
    @SerializedName("status") var status: Int? = 0,
) : Serializable