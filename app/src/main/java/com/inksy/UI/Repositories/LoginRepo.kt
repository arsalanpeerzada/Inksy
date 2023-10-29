package com.inksy.UI.Repositories

import androidx.lifecycle.MutableLiveData
import com.inksy.Model.UserModel
import com.inksy.Remote.APIClient
import com.inksy.Remote.APIInterface
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class LoginRepo {
    var apiInterface: APIInterface = APIClient.createService(APIInterface::class.java)

    companion object {
        lateinit var loginRepo: LoginRepo

        fun getInstance(): LoginRepo {

            loginRepo = LoginRepo()

            return loginRepo
        }
    }

    fun login(
        email: String?,
        password: String?,
        mobile: String?,
        code: String?,
        deviceToken: String?
    ): MutableLiveData<APIInterface.ApiResponse<UserModel>> {
        val login: MutableLiveData<APIInterface.ApiResponse<UserModel>> =
            MutableLiveData<APIInterface.ApiResponse<UserModel>>()
        apiInterface.login(email, password, mobile, code, "Android", deviceToken)
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

    fun loginRegister(
        email: String?,
        password: String?,
        mobile: String?,
        code: String?,
        tokenCode : String,
        deviceToken : String
    ): MutableLiveData<APIInterface.ApiResponse<UserModel>> {
        val login: MutableLiveData<APIInterface.ApiResponse<UserModel>> =
            MutableLiveData<APIInterface.ApiResponse<UserModel>>()
        apiInterface.loginRegister(email, password, mobile, code,tokenCode, "Android", deviceToken)
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
                }
            })
        return login
    }


}
