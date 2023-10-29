package com.inksy.Model

import com.example.example.DoodlePack
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class UserModel(
    @SerializedName("token") var token: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("full_name") var fullName: String? = null,
    @SerializedName("bio") var bio: String? = null,
    @SerializedName("is_artist") var is_artist: Int? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("username") var username: String? = null,
    @SerializedName("phone_code") var phoneCode: String? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("avatar") var avatar: String? = null,
    @SerializedName("payment_email") var payment_email: String? = null,
    @SerializedName("payment_method") var payment_method: String? = null,
    @SerializedName("utype") var utype: Int? = null,
    @SerializedName("points") var points: Int? = null,
    @SerializedName("follower_count") var followerCount: Int? = null,
    @SerializedName("following_count") var followingCount: Int? = null,
    @SerializedName("device_type") var deviceType: String? = null,
    @SerializedName("device_token") var deviceToken: String? = null,
    @SerializedName("is_followed") var isFollowed: IsFollowed? = null,
    @SerializedName("is_private_profile") var isPrivateProfile: Int? = null,
    @SerializedName("is_profile_completed") var isProfileCompleted: Int? = null,
    @SerializedName("journals") var journals: ArrayList<Journals>? = null,
    @SerializedName("doodles") var doodles: ArrayList<DoodlePack>? = null,
    @SerializedName("followers") var followers: ArrayList<UserModel>? = null,
    @SerializedName("is_email_verification") var is_email_verification: Int = 0,
    @SerializedName("is_active") var isActive: Int? = null,





) : Serializable
