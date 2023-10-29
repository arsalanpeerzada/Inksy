package com.inksy.Model

import com.google.gson.annotations.SerializedName

data class SendCommentData(
    @SerializedName("journal_id") var journalId: String? = null,
    @SerializedName("comment") var comment: String? = null,
    @SerializedName("user_id") var userId: Int? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("id") var id: Int? = null
)
