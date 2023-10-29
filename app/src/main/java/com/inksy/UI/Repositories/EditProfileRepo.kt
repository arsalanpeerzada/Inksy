package com.inksy.UI.Repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonElement
import com.inksy.Model.UserModel
import com.inksy.Remote.APIClient
import com.inksy.Remote.APIInterface
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EditProfileRepo {
    var apiInterface: APIInterface = APIClient.createService(APIInterface::class.java)

    companion object {
        lateinit var editProfileRepo: EditProfileRepo

        fun getInstance(): EditProfileRepo {

            editProfileRepo = EditProfileRepo()

            return editProfileRepo
        }
    }

    fun changepassword(
        email: String?,
        password: String?,
        confirmPassword: String?,
        oldpassword: String?,
        token: String?
    ): MutableLiveData<APIInterface.ApiResponse<JsonElement>> {
        val changePassword: MutableLiveData<APIInterface.ApiResponse<JsonElement>> =
            MutableLiveData<APIInterface.ApiResponse<JsonElement>>()
        val mytoken = "Bearer $token"
        apiInterface.changePassword(oldpassword, password, confirmPassword, email, mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<JsonElement>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<JsonElement>?>?,
                    response: Response<APIInterface.ApiResponse<JsonElement>>
                ) {
                    if (response.isSuccessful) {
                        changePassword.value = response.body()
                    }
                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<JsonElement>>,
                    t: Throwable?
                ) {

                }
            })
        return changePassword
    }

    fun userprofile(
        _fullname: String?,
        _bio: String?,
        _avatar: RequestBody,
        _token: String,
    ): MutableLiveData<APIInterface.ApiResponse<UserModel>> {
        val login: MutableLiveData<APIInterface.ApiResponse<UserModel>> =
            MutableLiveData<APIInterface.ApiResponse<UserModel>>()


        val mytoken = "Bearer $_token"
        val fullname: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), _fullname!!)
        val bio: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), _bio!!)
        val avatar: RequestBody = _avatar


        apiInterface.profile(avatar, fullname, bio, mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<UserModel>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<UserModel>?>?,
                    response: Response<APIInterface.ApiResponse<UserModel>>
                ) {
                    if (response.isSuccessful) {
                        login.value = response.body()
                    }
                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<UserModel>>,
                    t: Throwable?
                ) {
                    Log.d("error", t?.message.toString())
                }
            })
        return login
    }

    fun userprofile(
        _fullname: String?,
        _bio: String?,
        _token: String,
    ): MutableLiveData<APIInterface.ApiResponse<UserModel>> {
        val login: MutableLiveData<APIInterface.ApiResponse<UserModel>> =
            MutableLiveData<APIInterface.ApiResponse<UserModel>>()


        val mytoken = "Bearer $_token"
        val fullname: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), _fullname!!)
        val bio: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), _bio!!)


        apiInterface.profile(fullname, bio, mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<UserModel>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<UserModel>?>?,
                    response: Response<APIInterface.ApiResponse<UserModel>>
                ) {
                    if (response.isSuccessful) {
                        login.value = response.body()
                    }
                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<UserModel>>,
                    t: Throwable?
                ) {
                    Log.d("error", t?.message.toString())
                }
            })
        return login
    }


    fun changePrivacy(
        token: String,
    ): MutableLiveData<APIInterface.ApiResponse<UserModel>> {
        val login: MutableLiveData<APIInterface.ApiResponse<UserModel>> =
            MutableLiveData<APIInterface.ApiResponse<UserModel>>()

        val mytoken = "Bearer $token"
        apiInterface.privacyChange(mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<UserModel>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<UserModel>?>?,
                    response: Response<APIInterface.ApiResponse<UserModel>>
                ) {
                    if (response.isSuccessful) {
                        login.value = response.body()
                    }
                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<UserModel>>,
                    t: Throwable?
                ) {

                }
            })
        return login
    }






}
