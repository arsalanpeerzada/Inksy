package com.inksy.Model

import com.google.gson.annotations.SerializedName

data class AnalyticsData(
    @SerializedName("analytics") var analytics: Analytics = Analytics(),
    @SerializedName("price_range") var priceRange: String? = null,
    @SerializedName("orders") var analyticsOrders: ArrayList<AnalyticsOrders> = ArrayList()
)
