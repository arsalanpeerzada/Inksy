package com.inksy.UI.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonElement
import com.inksy.Model.UserModel
import com.inksy.Remote.APIInterface
import com.inksy.UI.Repositories.EditProfileRepo
import okhttp3.RequestBody


class EditProfileView : ViewModel() {

    private var mutableLiveData: MutableLiveData<APIInterface.ApiResponse<JsonElement>>? = null
    private var mutableLiveDataUser : MutableLiveData<APIInterface.ApiResponse<UserModel>>? = null
    private var editProfileRepo: EditProfileRepo? = null

    fun init() {
        editProfileRepo = EditProfileRepo.getInstance()
        if (mutableLiveData != null) {
            return
        }

    }
    fun changepassword(
        password: String?,
        confirmPassword: String?,
        token: String?,
        oldpassword: String?,
        email: String?
    ): LiveData<APIInterface.ApiResponse<JsonElement>?>? {
        mutableLiveData = editProfileRepo!!.changepassword(
            email,
            password,
            confirmPassword,
            oldpassword,
            token
        )
        return mutableLiveData
    }

    fun profile(
        fullname: String,
        bio: String,
        avatar: RequestBody,
        token: String
    ): LiveData<APIInterface.ApiResponse<UserModel>?>? {
        mutableLiveDataUser = editProfileRepo!!.userprofile(fullname, bio, avatar, token)
        return mutableLiveDataUser
    }

    fun profile(
        fullname: String,
        bio: String,
        token: String
    ): LiveData<APIInterface.ApiResponse<UserModel>?>? {
        mutableLiveDataUser = editProfileRepo!!.userprofile(fullname, bio, token)
        return mutableLiveDataUser
    }

    fun changePrivacy(
        token: String
    ): LiveData<APIInterface.ApiResponse<UserModel>?>? {
        mutableLiveDataUser = editProfileRepo!!.changePrivacy(token)
        return mutableLiveDataUser
    }
}