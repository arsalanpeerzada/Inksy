package com.example.example

import com.google.gson.annotations.SerializedName


data class DoodleData(
    @SerializedName("featured_pack") var featuredPack: ArrayList<DoodlePack> = arrayListOf(),
    @SerializedName("best_sellers") var pack: ArrayList<DoodlePack> = arrayListOf(),
    @SerializedName("all_packs") var allpack: ArrayList<DoodlePack> = arrayListOf(),
    @SerializedName("purchased_pack") var purchased_pack: ArrayList<DoodlePack> = arrayListOf(),
)