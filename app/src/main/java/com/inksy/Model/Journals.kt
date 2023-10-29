package com.inksy.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Journals(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("category_id") var categoryId: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("cover_image") var coverImage: String? = null,
    @SerializedName("cover_bc") var coverBc: String? = null,
    @SerializedName("cover_rgb") var coverRgb: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("is_active") var isActive: Int? = null,
    @SerializedName("is_appeal") var isAppeal: Int? = null,
    @SerializedName("created_by") var createdBy: Int? = null,
    @SerializedName("html_content") var htmlContent: String? = null,
    @SerializedName("protection") var protection: Int? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("is_journal_like") var isJournalLike: Int? = null,
    @SerializedName("is_journal_follow") var isJournalFollow: Int? = null,
    @SerializedName("likes_count") var likesCount: Int? = null,
    @SerializedName("comments_count") var commentsCount: Int? = null,
    @SerializedName("followers_count") var followersCount: Int? = null,
    @SerializedName("pages") var pages: ArrayList<Pages> = arrayListOf(),
    @SerializedName("category") var category: Category? = Category(),
    @SerializedName("user") var user: UserModel? = UserModel(),
    @SerializedName("specific_users") var specificUsers: ArrayList<String> = arrayListOf()


) : Serializable