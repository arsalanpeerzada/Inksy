package com.inksy.UI.Repositories

import androidx.lifecycle.MutableLiveData
import com.inksy.Model.ChatDataModel
import com.inksy.Model.ChatMainModel
import com.inksy.Model.UserModel
import com.inksy.Remote.APIClient
import com.inksy.Remote.APIInterface
import com.inksy.Remote.Resource
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChatRepo {
    var apiInterface: APIInterface = APIClient.createService(APIInterface::class.java)

    companion object {
        lateinit var chatRepo: ChatRepo

        fun getInstance(): ChatRepo {

            chatRepo = ChatRepo()

            return chatRepo
        }
    }

    fun chatList(
        token: String?
    ): MutableLiveData<Resource<APIInterface.ApiResponse<List<ChatDataModel>>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<List<ChatDataModel>>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<List<ChatDataModel>>>>()

        var mytoken = "Bearer $token"

        apiInterface.chatList(mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<List<ChatDataModel>>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<List<ChatDataModel>>>,
                    response: Response<APIInterface.ApiResponse<List<ChatDataModel>>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<List<ChatDataModel>> =
                            response.body()!!

                        if (body.status == 1) {
                            data.value = Resource.success(body)
                        } else {
                            data.value = Resource.error(body.message.toString(), null)
                        }


                    } else {
                        val body: ResponseBody? = response.errorBody()
                        try {
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            var string = jObjError.getString("message")

                            data.value = Resource.error(string, null)
                        } catch (e: Exception) {
                            // Toast.makeText(getContext(), e.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<List<ChatDataModel>>>,
                    t: Throwable?
                ) {
                    var dataa = t?.message.toString()
                    var mydata = t?.localizedMessage

                    data.value = Resource.error(dataa, null)
                }
            })
        return data
    }

}
