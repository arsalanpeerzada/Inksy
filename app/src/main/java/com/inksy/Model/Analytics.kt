package com.inksy.Model

import com.google.gson.annotations.SerializedName

data class Analytics(
    @SerializedName("total_pack") var totalPack: Double = 0.0,
    @SerializedName("approved_pack") var approvedPack: Double = 0.0,
    @SerializedName("pending_pack") var pendingPack: Double = 0.0,
    @SerializedName("total_sales") var totalSales: Double = 0.0,
    @SerializedName("today_sales") var todaySales: Double = 0.0,
    @SerializedName("monthly_sales") var monthlySales: Double = 0.0,
    @SerializedName("yealry_sales") var yealrySales: Double = 0.0,
    @SerializedName("total_received") var totalReceived: Double = 0.0,
    @SerializedName("total_earned") var totalEarned: Double = 0.0
)
