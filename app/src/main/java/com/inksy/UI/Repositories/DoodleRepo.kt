package com.inksy.UI.Repositories

import androidx.lifecycle.MutableLiveData
import com.example.example.DoodleData
import com.example.example.DoodlePack
import com.google.gson.JsonElement
import com.inksy.Model.AnalyticsData
import com.inksy.Model.CreateDoodleModel
import com.inksy.Model.CreateOrderData
import com.inksy.Model.TransactionModel
import com.inksy.Remote.APIClient
import com.inksy.Remote.APIInterface
import com.inksy.Remote.Resource
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class DoodleRepo {
    var apiInterface: APIInterface = APIClient.createService(APIInterface::class.java)

    companion object {
        lateinit var doodleRepo: DoodleRepo

        fun getInstance(): DoodleRepo {

            doodleRepo = DoodleRepo()

            return doodleRepo
        }
    }


    fun searchDoodle(
        searchText: String?,
        token: String?
    ): MutableLiveData<Resource<APIInterface.ApiResponse<List<DoodlePack>>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<List<DoodlePack>>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<List<DoodlePack>>>>()


        apiInterface.searchDoodle(searchText, token)
            ?.enqueue(object : Callback<APIInterface.ApiResponse<List<DoodlePack>>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<List<DoodlePack>>>,
                    response: Response<APIInterface.ApiResponse<List<DoodlePack>>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<List<DoodlePack>> = response.body()!!

                        data.value = Resource.success(body)

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
                    call: Call<APIInterface.ApiResponse<List<DoodlePack>>>,
                    t: Throwable?
                ) {
                    var dataa = t?.message.toString()
                    var mydata = t?.localizedMessage

                    data.value = Resource.error(dataa, null)
                }
            })
        return data
    }


    fun getDoodle(
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<DoodleData>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<DoodleData>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<DoodleData>>>()

        val mytoken = "Bearer $token"
        apiInterface.doodleShop(mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<DoodleData>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<DoodleData>?>?,
                    response: Response<APIInterface.ApiResponse<DoodleData>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<DoodleData> = response.body()!!

                        data.value = Resource.success(body)

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
                    call: Call<APIInterface.ApiResponse<DoodleData>>,
                    t: Throwable?
                ) {
                    var dataa = t?.message.toString()
                    var mydata = t?.localizedMessage

                    data.value = Resource.error(dataa, null)
                }
            })
        return data
    }

    fun getDoodleAll(
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<DoodleData>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<DoodleData>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<DoodleData>>>()

        val mytoken = "Bearer $token"
        apiInterface.doodleShopAll(mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<DoodleData>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<DoodleData>?>?,
                    response: Response<APIInterface.ApiResponse<DoodleData>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<DoodleData> = response.body()!!

                        data.value = Resource.success(body)

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
                    call: Call<APIInterface.ApiResponse<DoodleData>>,
                    t: Throwable?
                ) {
                    var dataa = t?.message.toString()
                    var mydata = t?.localizedMessage

                    data.value = Resource.error(dataa, null)
                }
            })
        return data
    }

    fun doodlePending(
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<List<DoodlePack>>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<List<DoodlePack>>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<List<DoodlePack>>>>()

        val mytoken = "Bearer $token"
        apiInterface.doodlePending(mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<List<DoodlePack>>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<List<DoodlePack>>?>?,
                    response: Response<APIInterface.ApiResponse<List<DoodlePack>>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<List<DoodlePack>> = response.body()!!

                        data.value = Resource.success(body)

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
                    call: Call<APIInterface.ApiResponse<List<DoodlePack>>>,
                    t: Throwable?
                ) {
                    var dataa = t?.message.toString()
                    var mydata = t?.localizedMessage

                    data.value = Resource.error(dataa, null)
                }
            })
        return data
    }

    fun doodleAppoved(
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<List<DoodlePack>>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<List<DoodlePack>>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<List<DoodlePack>>>>()

        val mytoken = "Bearer $token"
        apiInterface.doodleApproved(mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<List<DoodlePack>>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<List<DoodlePack>>?>?,
                    response: Response<APIInterface.ApiResponse<List<DoodlePack>>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<List<DoodlePack>> = response.body()!!

                        data.value = Resource.success(body)

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
                    call: Call<APIInterface.ApiResponse<List<DoodlePack>>>,
                    t: Throwable?
                ) {
                    var dataa = t?.message.toString()
                    var mydata = t?.localizedMessage

                    data.value = Resource.error(dataa, null)
                }
            })
        return data
    }

    fun doodlePurchased(
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<List<DoodlePack>>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<List<DoodlePack>>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<List<DoodlePack>>>>()

        val mytoken = "Bearer $token"
        apiInterface.doodlePurchased(mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<List<DoodlePack>>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<List<DoodlePack>>?>?,
                    response: Response<APIInterface.ApiResponse<List<DoodlePack>>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<List<DoodlePack>> = response.body()!!

                        data.value = Resource.success(body)

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
                    call: Call<APIInterface.ApiResponse<List<DoodlePack>>>,
                    t: Throwable?
                ) {
                    var dataa = t?.message.toString()
                    var mydata = t?.localizedMessage

                    data.value = Resource.error(dataa, null)
                }
            })
        return data
    }


    fun artistDashboard(
        token: String,
        type: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<AnalyticsData>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<AnalyticsData>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<AnalyticsData>>>()

        val mytoken = "Bearer $token"
        apiInterface.artistDashboard(mytoken, type)
            .enqueue(object : Callback<APIInterface.ApiResponse<AnalyticsData>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<AnalyticsData>?>?,
                    response: Response<APIInterface.ApiResponse<AnalyticsData>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<AnalyticsData> = response.body()!!

                        data.value = Resource.success(body)

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
                    call: Call<APIInterface.ApiResponse<AnalyticsData>>,
                    t: Throwable?
                ) {
                    var dataa = t?.message.toString()
                    var mydata = t?.localizedMessage

                    data.value = Resource.error(dataa, null)
                }
            })
        return data
    }

    fun createpack(
        token: String,
        _packTitle: String,
        _price: String,
        _defaultCover: File,
    ): MutableLiveData<Resource<APIInterface.ApiResponse<CreateDoodleModel>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<CreateDoodleModel>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<CreateDoodleModel>>>()


        val packTitle: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), _packTitle)
        val price: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), _price.toString())
        var cover_image: RequestBody
        cover_image = RequestBody.create(".png".toMediaTypeOrNull(), _defaultCover)

        val mytoken = "Bearer $token"
        apiInterface.createPack(mytoken, packTitle, price, cover_image)
            .enqueue(object : Callback<APIInterface.ApiResponse<CreateDoodleModel>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<CreateDoodleModel>?>?,
                    response: Response<APIInterface.ApiResponse<CreateDoodleModel>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<CreateDoodleModel> = response.body()!!

                        data.value = Resource.success(body)

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
                    call: Call<APIInterface.ApiResponse<CreateDoodleModel>>,
                    t: Throwable?
                ) {
                    var dataa = t?.message.toString()
                    var mydata = t?.localizedMessage

                    data.value = Resource.error(dataa, null)
                }
            })
        return data
    }


    fun makeArtist(
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>()

        val mytoken = "Bearer $token"
        apiInterface.artistMake(mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<JsonElement>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<JsonElement>?>?,
                    response: Response<APIInterface.ApiResponse<JsonElement>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<JsonElement> = response.body()!!

                        data.value = Resource.success(body)

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
                    call: Call<APIInterface.ApiResponse<JsonElement>>,
                    t: Throwable?
                ) {
                    var dataa = t?.message.toString()
                    var mydata = t?.localizedMessage

                    data.value = Resource.error(dataa, null)
                }
            })
        return data
    }

    fun createOrder(
        doodle_pack_id: Int,
        amount: Double,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? {

        val data: MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>? =
            MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>()

        val mytoken = "Bearer $token"
        apiInterface.orderCreate(doodle_pack_id, amount.toString(), mytoken).enqueue(
            object : Callback<APIInterface.ApiResponse<JsonElement>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<JsonElement>>,
                    response: Response<APIInterface.ApiResponse<JsonElement>>
                ) {
                    val body: APIInterface.ApiResponse<JsonElement> = response.body()!!

                    data?.value = Resource.success(body)
                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<JsonElement>>,
                    t: Throwable
                ) {
                    var dataa = t?.message.toString()
                    var mydata = t?.localizedMessage

                    data?.value = Resource.error(dataa, null)
                }

            }
        )
        return data
    }


    fun getDetails(
        doodle_pack_id: String,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<DoodlePack>>>? {

        val data: MutableLiveData<Resource<APIInterface.ApiResponse<DoodlePack>>>? =
            MutableLiveData<Resource<APIInterface.ApiResponse<DoodlePack>>>()

        val mytoken = "Bearer $token"
        apiInterface.doodleDetails(doodle_pack_id, mytoken).enqueue(
            object : Callback<APIInterface.ApiResponse<DoodlePack>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<DoodlePack>>,
                    response: Response<APIInterface.ApiResponse<DoodlePack>>
                ) {
                    val body: APIInterface.ApiResponse<DoodlePack> = response.body()!!

                    data?.value = Resource.success(body)
                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<DoodlePack>>,
                    t: Throwable
                ) {
                    var dataa = t?.message.toString()
                    var mydata = t?.localizedMessage

                    data?.value = Resource.error(dataa, null)
                }

            }
        )
        return data
    }

    fun addDoodle(
        doodle_pack_id: RequestBody,
        doodle_image: RequestBody,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<DoodlePack>>>? {

        val data: MutableLiveData<Resource<APIInterface.ApiResponse<DoodlePack>>>? =
            MutableLiveData<Resource<APIInterface.ApiResponse<DoodlePack>>>()

        val mytoken = "Bearer $token"
        apiInterface.addDoodle(doodle_pack_id, doodle_image, mytoken).enqueue(
            object : Callback<APIInterface.ApiResponse<DoodlePack>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<DoodlePack>>,
                    response: Response<APIInterface.ApiResponse<DoodlePack>>
                ) {
                    val body: APIInterface.ApiResponse<DoodlePack> = response.body()!!

                    data?.value = Resource.success(body)
                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<DoodlePack>>,
                    t: Throwable
                ) {
                    var dataa = t?.message.toString()
                    var mydata = t?.localizedMessage

                    data?.value = Resource.error(dataa, null)
                }

            }
        )
        return data
    }

    fun getTransaction(
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<List<TransactionModel>>>>? {

        val data: MutableLiveData<Resource<APIInterface.ApiResponse<List<TransactionModel>>>>? =
            MutableLiveData<Resource<APIInterface.ApiResponse<List<TransactionModel>>>>()

        val mytoken = "Bearer $token"
        apiInterface.artistTransaction(mytoken).enqueue(
            object : Callback<APIInterface.ApiResponse<List<TransactionModel>>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<List<TransactionModel>>>,
                    response: Response<APIInterface.ApiResponse<List<TransactionModel>>>
                ) {
                    val body: APIInterface.ApiResponse<List<TransactionModel>> = response.body()!!

                    data?.value = Resource.success(body)
                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<List<TransactionModel>>>,
                    t: Throwable
                ) {
                    var dataa = t?.message.toString()
                    var mydata = t?.localizedMessage

                    data?.value = Resource.error(dataa, null)
                }

            }
        )
        return data
    }


    fun deletePack(
        doodle_pack_id: Int,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>()

        val mytoken = "Bearer $token"
        apiInterface.deletePack(doodle_pack_id, mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<JsonElement>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<JsonElement>?>?,
                    response: Response<APIInterface.ApiResponse<JsonElement>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<JsonElement> = response.body()!!

                        data.value = Resource.success(body)

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
                    call: Call<APIInterface.ApiResponse<JsonElement>>,
                    t: Throwable?
                ) {
                    var dataa = t?.message.toString()
                    var mydata = t?.localizedMessage

                    data.value = Resource.error(dataa, null)
                }
            })
        return data
    }


    fun deleteDoodle(
        doodleID: Int,
        doodle_pack_id: Int,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>()

        val mytoken = "Bearer $token"
        apiInterface.deleteDoodle(doodleID, doodle_pack_id, mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<JsonElement>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<JsonElement>?>?,
                    response: Response<APIInterface.ApiResponse<JsonElement>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<JsonElement> = response.body()!!

                        data.value = Resource.success(body)

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
                    call: Call<APIInterface.ApiResponse<JsonElement>>,
                    t: Throwable?
                ) {
                    var dataa = t?.message.toString()
                    var mydata = t?.localizedMessage

                    data.value = Resource.error(dataa, null)
                }
            })
        return data
    }

    fun editDoodlePack(
        _pack_title: String,
        _price: String,
        _doodle_pack_id: Int,
        _cover_image: File,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>()

        val doodle_pack_id: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), _doodle_pack_id.toString())
        val packTitle: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), _pack_title)
        val price: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), _price.toString())
        var cover_image: RequestBody
        cover_image = RequestBody.create(".png".toMediaTypeOrNull(), _cover_image)

        val mytoken = "Bearer $token"
        apiInterface.updateDoodlePack(packTitle, price, doodle_pack_id, cover_image, mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<JsonElement>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<JsonElement>?>?,
                    response: Response<APIInterface.ApiResponse<JsonElement>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<JsonElement> = response.body()!!

                        data.value = Resource.success(body)

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
                    call: Call<APIInterface.ApiResponse<JsonElement>>,
                    t: Throwable?
                ) {
                    var dataa = t?.message.toString()
                    var mydata = t?.localizedMessage

                    data.value = Resource.error(dataa, null)
                }
            })
        return data
    }


//    fun editDoodlePack(
//        _pack_title: String,
//        _price: String,
//        _doodle_pack_id: Int,
//        _cover_image: File,
//        token: String,
//    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>> {
//        val data: MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>> =
//            MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>()
//
//        val doodle_pack_id: RequestBody =
//            RequestBody.create("text/plain".toMediaTypeOrNull(), _doodle_pack_id.toString())
//        val packTitle: RequestBody =
//            RequestBody.create("text/plain".toMediaTypeOrNull(), _pack_title)
//        val price: RequestBody =
//            RequestBody.create("text/plain".toMediaTypeOrNull(), _price.toString())
//        var cover_image: RequestBody
//        cover_image = RequestBody.create(".png".toMediaTypeOrNull(), _cover_image)
//
//        val mytoken = "Bearer $token"
//        apiInterface.updateDoodlePacktest(_pack_title, _price, _doodle_pack_id, cover_image, mytoken)
//            .enqueue(object : Callback<APIInterface.ApiResponse<JsonElement>> {
//                override fun onResponse(
//                    call: Call<APIInterface.ApiResponse<JsonElement>?>?,
//                    response: Response<APIInterface.ApiResponse<JsonElement>>
//                ) {
//                    if (response.body() != null) {
//                        val body: APIInterface.ApiResponse<JsonElement> = response.body()!!
//
//                        data.value = Resource.success(body)
//
//                    } else {
//                        val body: ResponseBody? = response.errorBody()
//                        try {
//                            val jObjError = JSONObject(response.errorBody()!!.string())
//                            var string = jObjError.getString("message")
//
//                            data.value = Resource.error(string, null)
//                        } catch (e: Exception) {
//                            // Toast.makeText(getContext(), e.message, Toast.LENGTH_LONG).show()
//                        }
//                    }
//                }
//
//                override fun onFailure(
//                    call: Call<APIInterface.ApiResponse<JsonElement>>,
//                    t: Throwable?
//                ) {
//                    var dataa = t?.message.toString()
//                    var mydata = t?.localizedMessage
//
//                    data.value = Resource.error(dataa, null)
//                }
//            })
//        return data
//    }

}