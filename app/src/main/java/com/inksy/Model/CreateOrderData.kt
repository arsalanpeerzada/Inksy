package com.inksy.Model

import com.google.gson.annotations.SerializedName

data class CreateOrderData(
    @SerializedName("doodle_pack_id") var doodlePackId: Int? = null,
    @SerializedName("pack_title") var packTitle: String? = null,
    @SerializedName("artist_id") var artistId: Int? = null,
    @SerializedName("user_id") var userId: Int? = null,
    @SerializedName("total_amount") var totalAmount: Int? = null,
    @SerializedName("admin_earning") var adminEarning: Int? = null,
    @SerializedName("artist_earning") var artistEarning: Int? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("id") var id: Int? = null
)
