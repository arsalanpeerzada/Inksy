package com.inksy.Model

import com.example.example.Doodles
import com.google.gson.annotations.SerializedName

data class DashboardDataModel(
    @SerializedName("people") var people: People? = null,
    @SerializedName("journals") var journals: ArrayList<Journals>? = null,
    @SerializedName("followed_journals") var followedJournals: ArrayList<Journals>? = null,
    @SerializedName("categories") var categories: ArrayList<Categories>? = null,
    @SerializedName("user_doodles") var userDoodles: ArrayList<Doodles> = arrayListOf()
)
