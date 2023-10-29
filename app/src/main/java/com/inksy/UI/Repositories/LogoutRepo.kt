package com.inksy.UI.Repositories

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonElement
import com.inksy.Remote.APIClient
import com.inksy.Remote.APIInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LogoutRepo {
    var apiInterface: APIInterface = APIClient.createService(APIInterface::class.java)

    companion object {
        lateinit var logoutRepo: LogoutRepo

        fun getInstance(): LogoutRepo {

            logoutRepo = LogoutRepo()

            return logoutRepo
        }
    }

    fun logout(token: String?): MutableLiveData<APIInterface.ApiResponse<JsonElement>> {
        val logout: MutableLiveData<APIInterface.ApiResponse<JsonElement>> =
            MutableLiveData<APIInterface.ApiResponse<JsonElement>>()

        val mytoken = "Bearer $token"
        apiInterface.logout(mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<JsonElement>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<JsonElement>>,
                    response: Response<APIInterface.ApiResponse<JsonElement>>
                ) {
                    if (response.isSuccessful) {
                        logout.value = response.body()
                    }
                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<JsonElement>>,
                    t: Throwable?
                ) {

                }
            })
        return logout
    }


}
