package com.example.example

import com.google.gson.annotations.SerializedName
import com.inksy.Model.UserModel
import java.io.Serializable


data class DoodlePack(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("pack_title") var packTitle: String? = null,
    @SerializedName("cover_image") var coverImage: String? = null,
    @SerializedName("price") var price: Double? = null,
    @SerializedName("sales_count") var salesCount: Int? = null,
    @SerializedName("created_by") var createdBy: Int? = null,
    @SerializedName("is_admin") var isAdmin: Int? = null,
    @SerializedName("is_active") var isActive: Int? = null,
    @SerializedName("is_featured") var isFeatured: Int? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("is_purchased") var isPurchased: Int? = null,
    @SerializedName("doodles") var doodles: ArrayList<Doodles> = arrayListOf(),
    @SerializedName("user") var user: UserModel? = UserModel()

) : Serializable