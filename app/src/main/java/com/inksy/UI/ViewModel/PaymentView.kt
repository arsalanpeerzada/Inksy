package com.inksy.UI.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonElement
import com.inksy.Model.ChatDataModel
import com.inksy.Remote.APIInterface
import com.inksy.Remote.Resource
import com.inksy.UI.Repositories.PaymentRepo


class PaymentView : ViewModel() {

    private var mutableLiveDataPayment: MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? =
        null
    private var paymentRepo: PaymentRepo? = null

    fun init() {
        paymentRepo = PaymentRepo.getInstance()
        if (mutableLiveDataPayment != null) {
            return
        }
    }

    fun paymentMethod(
        paymentEmail: String,
        paymentMethod: String,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? {
        mutableLiveDataPayment = paymentRepo!!.paymentMethod(paymentEmail, paymentMethod, token)
        return mutableLiveDataPayment
    }

}