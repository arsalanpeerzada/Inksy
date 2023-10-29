package com.inksy.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TransactionModel(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("user_id") var userId: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("transaction_image") var transactionImage: String? = null,
    @SerializedName("amount") var amount: Double? = null,
    @SerializedName("created_by") var createdBy: Int? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null
) : Serializable
