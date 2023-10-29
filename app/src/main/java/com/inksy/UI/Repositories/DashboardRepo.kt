package com.inksy.UI.Repositories

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.inksy.Model.DashboardDataModel
import com.inksy.Model.Journals
import com.inksy.Model.UserModel
import com.inksy.Remote.APIClient
import com.inksy.Remote.APIInterface
import com.inksy.Remote.Resource
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.AccessController.getContext


class DashboardRepo {
    var apiInterface: APIInterface = APIClient.createService(APIInterface::class.java)

    companion object {
        lateinit var dashboardRepo: DashboardRepo

        fun getInstance(): DashboardRepo {

            dashboardRepo = DashboardRepo()

            return dashboardRepo
        }
    }

    fun getData(
        token: String?
    ): MutableLiveData<Resource<APIInterface.ApiResponse<DashboardDataModel>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<DashboardDataModel>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<DashboardDataModel>>>()


        apiInterface.dashboardData(token)
            .enqueue(object : Callback<APIInterface.ApiResponse<DashboardDataModel>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<DashboardDataModel>?>?,
                    response: Response<APIInterface.ApiResponse<DashboardDataModel>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<DashboardDataModel> = response.body()!!

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
                    call: Call<APIInterface.ApiResponse<DashboardDataModel>>,
                    t: Throwable?
                ) {
                    var dataa = t?.message.toString()
                    var mydata = t?.localizedMessage

                    data.value = Resource.error(dataa, null)
                }
            })
        return data
    }

    fun searchUser(
        searchText: String?,
        token: String?
    ): MutableLiveData<Resource<APIInterface.ApiResponse<List<UserModel>>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<List<UserModel>>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<List<UserModel>>>>()


        apiInterface.searchUser(searchText, token).enqueue(object : Callback<APIInterface.ApiResponse<List<UserModel>>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<List<UserModel>>>,
                    response: Response<APIInterface.ApiResponse<List<UserModel>>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<List<UserModel>> = response.body()!!

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
                    call: Call<APIInterface.ApiResponse<List<UserModel>>>,
                    t: Throwable?
                ) {
                    var dataa = t?.message.toString()
                    var mydata = t?.localizedMessage

                    data.value = Resource.error(dataa, null)
                }
            })
        return data
    }


    fun searchJournal(
        searchText: String?,
        token: String?
    ): MutableLiveData<Resource<APIInterface.ApiResponse<List<Journals>>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<List<Journals>>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<List<Journals>>>>()


        apiInterface.searchJournal(searchText, token).enqueue(object : Callback<APIInterface.ApiResponse<List<Journals>>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<List<Journals>>>,
                    response: Response<APIInterface.ApiResponse<List<Journals>>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<List<Journals>> = response.body()!!

                        data.value = Resource.success(body)

                    } else {
                        val body: ResponseBody? = response.errorBody()
                        try {
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            var string = jObjError.getString("message")

                            data.value = Resource.error(string, null)
                        } catch (e: Exception) {
                             //Toast.makeText(getContext(), e.message, Toast.LENGTH_LONG).show()
                            Log.d("JournalCHeck",e.message.toString())
                        }
                    }


                }

                override fun onFailure(
                    call: Call<APIInterface.ApiResponse<List<Journals>>>,
                    t: Throwable?
                ) {
                    var dataa = t?.message.toString()
                    var mydata = t?.localizedMessage

                    data.value = Resource.error(dataa, null)
                }
            })
        return data
    }
    fun searchCategory(
        searchText: String?,
        token: String?
    ): MutableLiveData<Resource<APIInterface.ApiResponse<List<Journals>>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<List<Journals>>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<List<Journals>>>>()


        apiInterface.searchCategory(searchText, token).enqueue(object : Callback<APIInterface.ApiResponse<List<Journals>>> {
            override fun onResponse(
                call: Call<APIInterface.ApiResponse<List<Journals>>>,
                response: Response<APIInterface.ApiResponse<List<Journals>>>
            ) {
                if (response.body() != null) {
                    val body: APIInterface.ApiResponse<List<Journals>> = response.body()!!

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
                call: Call<APIInterface.ApiResponse<List<Journals>>>,
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
