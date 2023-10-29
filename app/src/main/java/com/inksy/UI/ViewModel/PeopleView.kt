package com.inksy.UI.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonElement
import com.inksy.Model.NumberVerifyModel
import com.inksy.Model.UserModel
import com.inksy.Remote.APIInterface
import com.inksy.Remote.Resource
import com.inksy.UI.Repositories.PeopleRepo


class PeopleView : ViewModel() {

    private var mutableLiveData: MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? =
        null
    private var mutableLiveDataUser: MutableLiveData<Resource<APIInterface.ApiResponse<UserModel>>>? =
        null

    private var mutableLiveDataUserVerify: MutableLiveData<Resource<APIInterface.ApiResponse<NumberVerifyModel>>>? =
        null
    private var mutableLiveDataPeople: MutableLiveData<Resource<APIInterface.ApiResponse<List<UserModel>>>>? =
        null
    private var peopleRepo: PeopleRepo? = null

    fun init() {
        peopleRepo = PeopleRepo.getInstance()
        if (mutableLiveData != null) {
            return
        }
    }

    fun userDetail(
        id: Int,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<UserModel>>>? {
        mutableLiveDataUser = peopleRepo!!.userDetails(id, token)
        return mutableLiveDataUser
    }


    fun blockList(
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<List<UserModel>>>>? {
        mutableLiveDataPeople = peopleRepo!!.getBlockList(token)
        return mutableLiveDataPeople
    }

    fun getfollowersFollowList(
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<List<UserModel>>>>? {
        mutableLiveDataPeople = peopleRepo!!.getfollowersFollowList(token)
        return mutableLiveDataPeople
    }

    fun forgotPassword(
        email: String,
        token: String?
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? {
        mutableLiveData = peopleRepo!!.forgotPassword(email, token)
        return mutableLiveData
    }

    fun verifyCode(
        code: String,
        email: String,
        token: String?
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? {
        mutableLiveData = peopleRepo!!.verifyCode(code, email, token)
        return mutableLiveData
    }

    fun phoneVerify(
        phoneCode: String,
        phone: String,
    ): MutableLiveData<Resource<APIInterface.ApiResponse<NumberVerifyModel>>>? {
        mutableLiveDataUserVerify = peopleRepo!!.phoneVerify(phoneCode, phone)
        return mutableLiveDataUserVerify
    }

    fun resetPassword(
        password: String,
        code: String,
        email: String,
        token: String?
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? {
        mutableLiveData = peopleRepo!!.resetPassword(password, code, email, token)
        return mutableLiveData
    }

    fun followRequests(
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<List<UserModel>>>>? {
        mutableLiveDataPeople = peopleRepo!!.followRequests(token)
        return mutableLiveDataPeople
    }


    fun userFollow(
        id: Int,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? {
        mutableLiveData = peopleRepo!!.followUser(id, token)
        return mutableLiveData
    }

    fun userBlock(
        id: Int,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? {
        mutableLiveData = peopleRepo!!.blockUser(id, token)
        return mutableLiveData
    }

    fun userUnblock(
        id: Int,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? {
        mutableLiveData = peopleRepo!!.unblockUser(id, token)
        return mutableLiveData
    }

    fun userUnfollow(
        id: Int,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? {
        mutableLiveData = peopleRepo!!.unfollowUser(id, token)
        return mutableLiveData
    }

    fun userRequest(
        userId: Int?,
        acceptChoice: Int?,
        token: String?
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? {
        mutableLiveData = peopleRepo!!.userRequest(userId!!, acceptChoice!!, token)
        return mutableLiveData
    }

    fun userReport(
        user_Id: Int,
        title: String,
        description: String,
        token: String,
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? {
        mutableLiveData = peopleRepo!!.userReport(
            user_Id,
            title,
            description,
            token!!
        )
        return mutableLiveData
    }
}