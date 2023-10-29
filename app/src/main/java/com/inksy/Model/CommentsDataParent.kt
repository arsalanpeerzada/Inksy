package com.inksy.Model

import com.google.gson.annotations.SerializedName

data class CommentsDataParent(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("created_by") var createdBy: Int? = null,
    @SerializedName("comments") var comments: ArrayList<CommentDataModel> = arrayListOf()
)
