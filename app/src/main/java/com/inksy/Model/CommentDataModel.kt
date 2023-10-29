package com.inksy.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CommentDataModel(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("parent_id") var parentId: Int? = null,
    @SerializedName("journal_id") var journalId: Int? = null,
    @SerializedName("comment") var comment: String? = null,
    @SerializedName("user_id") var userId: Int? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("is_comment_like") var isCommentLike: Int? = null,
    @SerializedName("likes_count") var likesCount: Int? = null,
    @SerializedName("user") var user: UserModel? = UserModel(),
    @SerializedName("replies") var replies : List<CommentDataModel> = ArrayList()
) : Serializable
