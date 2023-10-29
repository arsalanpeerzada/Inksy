package com.inksy.Model

import com.google.gson.annotations.SerializedName

data class Pages (

    @SerializedName("id"         ) var id        : Int?    = null,
    @SerializedName("journal_id" ) var journalId : Int?    = null,
    @SerializedName("page_no"    ) var pageNo    : Int?    = null,
    @SerializedName("page_image" ) var pageImage : String? = "",
    @SerializedName("created_at" ) var createdAt : String? = null,
    @SerializedName("updated_at" ) var updatedAt : String? = null

)
