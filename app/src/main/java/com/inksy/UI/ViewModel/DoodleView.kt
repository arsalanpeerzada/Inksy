package com.inksy.UI.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.example.DoodleData
import com.example.example.DoodlePack
import com.google.gson.JsonElement
import com.inksy.Model.AnalyticsData
import com.inksy.Model.CreateDoodleModel
import com.inksy.Model.CreateOrderData
import com.inksy.Model.TransactionModel
import com.inksy.Remote.APIInterface
import com.inksy.Remote.Resource
import com.inksy.UI.Repositories.DoodleRepo
import okhttp3.RequestBody
import java.io.File


class DoodleView : ViewModel() {
    private var mutableLiveDoodlePack: MutableLiveData<Resource<APIInterface.ApiResponse<List<DoodlePack>>>>? =
        null
    private var mutableLiveDoodleP: MutableLiveData<Resource<APIInterface.ApiResponse<DoodlePack>>>? =
        null
    private var mutableLiveDoodleData: MutableLiveData<Resource<APIInterface.ApiResponse<DoodleData>>>? =
        null
    private var mutableLiveData: MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? =
        null
    private var mutableLiveDataDashboard: MutableLiveData<Resource<APIInterface.ApiResponse<AnalyticsData>>>? =
        null
    private var mutableLiveDataCreate: MutableLiveData<Resource<APIInterface.ApiResponse<CreateDoodleModel>>>? =
        null
    private var mutableLiveCreateOrder: MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? =
        null
    private var mutableLiveTransaction: MutableLiveData<Resource<APIInterface.ApiResponse<List<TransactionModel>>>>? =
        null

    private var doodleRepo: DoodleRepo? = null

    fun init() {
        doodleRepo = DoodleRepo.getInstance()
        if (mutableLiveData != null) {
            return
        }
    }

    fun getData(token: String): MutableLiveData<Resource<APIInterface.ApiResponse<DoodleData>>>? {
        mutableLiveDoodleData = doodleRepo!!.getDoodle(token)
        return mutableLiveDoodleData
    }

    fun getDataAll(token: String): MutableLiveData<Resource<APIInterface.ApiResponse<DoodleData>>>? {
        mutableLiveDoodleData = doodleRepo!!.getDoodleAll(token)
        return mutableLiveDoodleData
    }

    fun doodlePending(token: String): MutableLiveData<Resource<APIInterface.ApiResponse<List<DoodlePack>>>>? {
        mutableLiveDoodlePack = doodleRepo!!.doodlePending(token)
        return mutableLiveDoodlePack
    }

    fun doodleApproved(token: String): MutableLiveData<Resource<APIInterface.ApiResponse<List<DoodlePack>>>>? {
        mutableLiveDoodlePack = doodleRepo!!.doodleAppoved(token)
        return mutableLiveDoodlePack
    }

    fun doodlePurchased(token: String): MutableLiveData<Resource<APIInterface.ApiResponse<List<DoodlePack>>>>? {
        mutableLiveDoodlePack = doodleRepo!!.doodlePurchased(token)
        return mutableLiveDoodlePack
    }

    fun artistDashboard(token: String, type: String): MutableLiveData<Resource<APIInterface.ApiResponse<AnalyticsData>>>? {
        mutableLiveDataDashboard = doodleRepo!!.artistDashboard(token, type)
        return mutableLiveDataDashboard
    }

    fun createPack(
        token: String, _packTitle: String,
        _price: String,
        _defaultCover: File,
    ): MutableLiveData<Resource<APIInterface.ApiResponse<CreateDoodleModel>>>? {
        mutableLiveDataCreate =
            doodleRepo!!.createpack(token, _packTitle, _price, _defaultCover)
        return mutableLiveDataCreate
    }

    fun artistMake(token: String): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? {
        mutableLiveData = doodleRepo!!.makeArtist(token)
        return mutableLiveData
    }

    fun searchDoodle(
        searchUser: String,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<List<DoodlePack>>>>? {
        mutableLiveDoodlePack = doodleRepo!!.searchDoodle(searchUser, token)
        return mutableLiveDoodlePack
    }


    fun createOrder(
        doodle_pack_id: Int, amount: Double, token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? {
        mutableLiveCreateOrder = doodleRepo!!.createOrder(doodle_pack_id, amount, token)
        return mutableLiveCreateOrder
    }

    fun doodleDetails(
        doodle_pack_id: String, token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<DoodlePack>>>? {
        mutableLiveDoodleP = doodleRepo!!.getDetails(doodle_pack_id, token)
        return mutableLiveDoodleP
    }

    fun addDoodle(
        doodle_pack_id: RequestBody, doodleImage: RequestBody, token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<DoodlePack>>>? {
        mutableLiveDoodleP = doodleRepo!!.addDoodle(doodle_pack_id, doodleImage, token)
        return mutableLiveDoodleP
    }

    fun getTransaction(
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<List<TransactionModel>>>>? {
        mutableLiveTransaction = doodleRepo!!.getTransaction(token)
        return mutableLiveTransaction
    }

    fun deletePack(
        doodle_pack_id: Int,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? {
        mutableLiveData = doodleRepo!!.deletePack(doodle_pack_id, token)
        return mutableLiveData
    }

    fun deleteDoodle(
        doodleID: Int,
        doodle_pack_id: Int,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? {
        mutableLiveData = doodleRepo!!.deleteDoodle(doodleID, doodle_pack_id, token)
        return mutableLiveData
    }

    fun editDoodle(
        pack_title: String,
        price: String,
        doodle_pack_id: Int,
        cover_Image: File,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? {
        mutableLiveData =
            doodleRepo!!.editDoodlePack(pack_title, price, doodle_pack_id, cover_Image, token)
        return mutableLiveData
    }

}