package com.inksy.UI.Repositories

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonElement
import com.inksy.Model.OthersModel
import com.inksy.Remote.APIClient
import com.inksy.Remote.APIInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OthersRepo {
    var apiInterface: APIInterface = APIClient.createService(APIInterface::class.java)

    companion object {
        lateinit var othersRepo: OthersRepo

        fun getInstance(): OthersRepo {

            othersRepo = OthersRepo()

            return othersRepo
        }
    }

    fun privacy(token: String): MutableLiveData<APIInterface.ApiResponse<OthersModel>> {
        val privacy: MutableLiveData<APIInterface.ApiResponse<OthersModel>> =
            MutableLiveData<APIInterface.ApiResponse<OthersModel>>()

        val mytoken = "Bearer $token"
        apiInterface.privacy_policy(mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<OthersModel>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<OthersModel>?>?,
                    response: Response<APIInterface.ApiResponse<OthersModel>>
                ) {
                    if (response.isSuccessful) {
                        privacy.value = response.body()
                    }
                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<OthersModel>>,
                    t: Throwable?
                ) {

                }
            })
        return privacy
    }

    fun about(token: String): MutableLiveData<APIInterface.ApiResponse<OthersModel>> {
        val about: MutableLiveData<APIInterface.ApiResponse<OthersModel>> =
            MutableLiveData<APIInterface.ApiResponse<OthersModel>>()

        val mytoken = "Bearer $token"
        apiInterface.about(mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<OthersModel>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<OthersModel>?>?,
                    response: Response<APIInterface.ApiResponse<OthersModel>>
                ) {
                    if (response.isSuccessful) {
                        about.value = response.body()
                    }
                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<OthersModel>>,
                    t: Throwable?
                ) {

                }
            })
        return about
    }

    fun community(token: String): MutableLiveData<APIInterface.ApiResponse<OthersModel>> {
        val about: MutableLiveData<APIInterface.ApiResponse<OthersModel>> =
            MutableLiveData<APIInterface.ApiResponse<OthersModel>>()

        val mytoken = "Bearer $token"
        apiInterface.community(mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<OthersModel>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<OthersModel>?>?,
                    response: Response<APIInterface.ApiResponse<OthersModel>>
                ) {
                    if (response.isSuccessful) {
                        about.value = response.body()
                    }
                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<OthersModel>>,
                    t: Throwable?
                ) {

                }
            })
        return about
    }

    fun terms(token: String): MutableLiveData<APIInterface.ApiResponse<OthersModel>> {
        val terms: MutableLiveData<APIInterface.ApiResponse<OthersModel>> =
            MutableLiveData<APIInterface.ApiResponse<OthersModel>>()

        val mytoken = "Bearer $token"
        apiInterface.terms(mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<OthersModel>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<OthersModel>?>?,
                    response: Response<APIInterface.ApiResponse<OthersModel>>
                ) {
                    if (response.isSuccessful) {
                        terms.value = response.body()
                    }
                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<OthersModel>>,
                    t: Throwable?
                ) {

                }
            })
        return terms
    }

    fun contactUs(
        subject: String,
        message: String,
        token: String,
    ): MutableLiveData<APIInterface.ApiResponse<JsonElement>> {
        val login: MutableLiveData<APIInterface.ApiResponse<JsonElement>> =
            MutableLiveData<APIInterface.ApiResponse<JsonElement>>()

        val mytoken = "Bearer $token"
        apiInterface.contactUs(subject, message, mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<JsonElement>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<JsonElement>?>?,
                    response: Response<APIInterface.ApiResponse<JsonElement>>
                ) {
                    if (response.isSuccessful) {
                        login.value = response.body()
                    }
                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<JsonElement>>,
                    t: Throwable?
                ) {

                }
            })
        return login
    }
    fun suggestion(
        category_name: String,
        description: String,
        token: String,
    ): MutableLiveData<APIInterface.ApiResponse<JsonElement>> {
        val login: MutableLiveData<APIInterface.ApiResponse<JsonElement>> =
            MutableLiveData<APIInterface.ApiResponse<JsonElement>>()

        val mytoken = "Bearer $token"
        apiInterface.suggestion(category_name, description, mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<JsonElement>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<JsonElement>?>?,
                    response: Response<APIInterface.ApiResponse<JsonElement>>
                ) {
                    if (response.isSuccessful) {
                        login.value = response.body()
                    }
                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<JsonElement>>,
                    t: Throwable?
                ) {

                }
            })
        return login
    }
}