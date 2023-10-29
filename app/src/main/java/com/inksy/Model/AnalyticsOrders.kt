package com.inksy.Model

import com.google.gson.annotations.SerializedName

data class AnalyticsOrders(
    @SerializedName("sales_count") var sales_count: Double = 0.0,
    @SerializedName("artist_earnings") var artist_earnings: Double = 0.0,
    @SerializedName("formated_date") var formated_date: Int = 0,
)
