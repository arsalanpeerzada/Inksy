package com.inksy.UI.Repositories

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonElement
import com.inksy.Database.Entities.PageTable
import com.inksy.Database.Entities.SelectedAudience
import com.inksy.Model.*
import com.inksy.Remote.APIClient
import com.inksy.Remote.APIInterface
import com.inksy.Remote.Resource
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class JournalRepo {
    var apiInterface: APIInterface = APIClient.createService(APIInterface::class.java)

    companion object {
        lateinit var journalRepo: JournalRepo

        fun getInstance(): JournalRepo {

            journalRepo = JournalRepo()

            return journalRepo
        }
    }

    fun reportJournal(
        journalTitle: String,
        journalDescription: String,
        journal_ID: Int,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>()
        var mytoken = "Bearer $token"

        apiInterface.journalReport(journal_ID, journalTitle, journalDescription, mytoken).enqueue(
            object : Callback<APIInterface.ApiResponse<JsonElement>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<JsonElement>>,
                    response: Response<APIInterface.ApiResponse<JsonElement>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<JsonElement> = response.body()!!

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
                    call: Call<APIInterface.ApiResponse<JsonElement>>,
                    t: Throwable
                ) {
                    var dataa = t?.message.toString()
                    var mydata = t?.localizedMessage

                    data.value = Resource.error(dataa, null)
                }
            }
        )
        return data
    }

    fun journalLike(
        journal_ID: String,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>()

        val mytoken = "Bearer $token"
        apiInterface.journalLike(journal_ID, mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<JsonElement>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<JsonElement>?>?,
                    response: Response<APIInterface.ApiResponse<JsonElement>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<JsonElement> = response.body()!!

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

    fun askForActivation(
        journal_ID: Int,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>()

        val mytoken = "Bearer $token"
        apiInterface.askForActivation(journal_ID, mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<JsonElement>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<JsonElement>?>?,
                    response: Response<APIInterface.ApiResponse<JsonElement>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<JsonElement> = response.body()!!

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

    fun journalDelete(
        journal_ID: String,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>()

        val mytoken = "Bearer $token"
        apiInterface.journalDelete(journal_ID, mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<JsonElement>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<JsonElement>?>?,
                    response: Response<APIInterface.ApiResponse<JsonElement>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<JsonElement> = response.body()!!

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

    fun journalFollow(
        journal_ID: String,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>()

        val mytoken = "Bearer $token"
        apiInterface.journalFollow(journal_ID, mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<JsonElement>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<JsonElement>?>?,
                    response: Response<APIInterface.ApiResponse<JsonElement>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<JsonElement> = response.body()!!

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


    fun journalDetails(
        journal_ID: Int,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<Journals>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<Journals>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<Journals>>>()

        val mytoken = "Bearer $token"
        apiInterface.journalDetail(journal_ID, mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<Journals>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<Journals>?>?,
                    response: Response<APIInterface.ApiResponse<Journals>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<Journals> = response.body()!!

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
                    call: Call<APIInterface.ApiResponse<Journals>>,
                    t: Throwable?
                ) {
                    var dataa = t?.message.toString()
                    var mydata = t?.localizedMessage

                    data.value = Resource.error(dataa, null)
                }
            })
        return data
    }


    fun journalCreate(
        _token: String,
        _category_id: Int,
        _title: String,
        _cover_bc: String,
        _description: String,
        _html_content: String,
        _protection: String,
        _is_active: String,
        _cover_image: File,
        _pageImage: File,
        selectedusers: List<SelectedAudience>,
        pages: List<PageTable>
    ): MutableLiveData<Resource<APIInterface.ApiResponse<Journals>>> {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        builder.addFormDataPart("cover_image", _cover_image.name, RequestBody.create("multipart/form-data".toMediaTypeOrNull(), _cover_image)
        )
        builder.addFormDataPart("category_id", _category_id.toString())
        builder.addFormDataPart("title", _title)
        builder.addFormDataPart("cover_bc", _cover_bc)
        builder.addFormDataPart("description", _description)
        builder.addFormDataPart("html_content", _html_content)
        builder.addFormDataPart("protection", _protection)
        builder.addFormDataPart("is_active", _is_active)
        builder.addFormDataPart("pages[0][page_no]", "0")
        builder.addFormDataPart("pages[0][page_image]", _pageImage.name, RequestBody.create("multipart/form-data".toMediaTypeOrNull(), _pageImage)
        )

        for (i in 0 until pages.size) {
            val image = File(pages[i].pageBackground!!)
            builder.addFormDataPart("pages[${i + 1}][page_no]", "${i + 1}")
            builder.addFormDataPart(
                "pages[${i + 1}][page_image]",
                image.name,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), image)
            )
        }

        if (selectedusers != null) {
            if (selectedusers.size > 0) {
                for (i in 0 until selectedusers.size) {
//                    if (i == 0) {
//                        selectedusers.get(i).userID.toString()
                    builder.addFormDataPart("user_ids[$i]", selectedusers[i].userID.toString())
//                    } else {
//                        tatee + "," + selectedusers.get(i).userID.toString()
//                    }
                }
            }
        }

        val requestBody = builder.build()

        val data: MutableLiveData<Resource<APIInterface.ApiResponse<Journals>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<Journals>>>()

        val mytoken = "Bearer $_token"
        apiInterface.journalCreate(
            mytoken,
            requestBody
        ).enqueue(object : Callback<APIInterface.ApiResponse<Journals>> {
            override fun onResponse(
                call: Call<APIInterface.ApiResponse<Journals>?>?,
                response: Response<APIInterface.ApiResponse<Journals>>
            ) {
                if (response.body() != null) {
                    val body: APIInterface.ApiResponse<Journals> = response.body()!!

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

                    }
                }
            }

            override fun onFailure(
                call: Call<APIInterface.ApiResponse<Journals>>,
                t: Throwable?
            ) {
                var dataa = t?.message.toString()
                var mydata = t?.localizedMessage

                data.value = Resource.error(dataa, null)
            }
        })
        return data
    }

    fun journalUpdate(
        _token: String,
        _category_id: Int,
        _title: String,
        _cover_bc: String,
        _description: String,
        _html_content: String,
        _protection: String,
        _is_active: String,
        _cover_image: File,
        dataimageString: String?,
        datacoverImage: String?,
        _page_Image: File,
        indexTemplateid: String,
        selectedusers: List<SelectedAudience>,
        journal_ID: String,
        pages: List<PageTable>,
        oldnumberofpages: Int,
    ): MutableLiveData<Resource<APIInterface.ApiResponse<Journals>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<Journals>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<Journals>>>()

        val mytoken = "Bearer $_token"
        var cover_image: RequestBody

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)

        builder.addFormDataPart("category_id", _category_id.toString())
        builder.addFormDataPart("title", _title)
        builder.addFormDataPart("cover_bc", _cover_bc)
        builder.addFormDataPart("description", _description)
        builder.addFormDataPart("html_content", _html_content)
        builder.addFormDataPart("protection", _protection)
        builder.addFormDataPart("is_active", _is_active)
        builder.addFormDataPart("journal_id", journal_ID)
        builder.addFormDataPart("pages[0][id]", indexTemplateid)
        builder.addFormDataPart("pages[0][page_image]", _page_Image.name, RequestBody.create("multipart/form-data".toMediaTypeOrNull(), _page_Image)
        )

        for (i in 0 until oldnumberofpages) {
            val image = File(pages[i].pageBackground!!)
            var imageString = pages[i].pageBackground!!

            var itemcheck = imageString[0]
            if (itemcheck == ('/')) {
                builder.addFormDataPart("pages[${i + 1}][id]", "${pages[i].pageBackgroundid}]")
                builder.addFormDataPart(
                    "pages[${i + 1}][page_image]",
                    image.name,
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), image)
                )
            }

        }
        for (i in oldnumberofpages until pages.size) {
            val image = File(pages[i].pageBackground!!)
            builder.addFormDataPart("pages[${i + 1}][page_no]", "${i + 1}")
            builder.addFormDataPart(
                "pages[${i + 1}][page_image]",
                image.name,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), image)
            )
        }

        if (dataimageString == "Empty") {
            if (selectedusers != null) {
                if (selectedusers.size > 0) {
                    for (i in 0 until selectedusers.size) {
                        builder.addFormDataPart(
                            "user_ids[" + i + "]",
                            selectedusers.get(i).userID.toString()
                        )
                    }
//
                }
            }

            val requestBody = builder.build()

            apiInterface.journalUpdate2(
                mytoken,
                requestBody,
            ).enqueue(object : Callback<APIInterface.ApiResponse<Journals>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<Journals>?>?,
                    response: Response<APIInterface.ApiResponse<Journals>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<Journals> = response.body()!!

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
                    call: Call<APIInterface.ApiResponse<Journals>>,
                    t: Throwable?
                ) {
                    var dataa = t?.message.toString()
                    var mydata = t?.localizedMessage

                    data.value = Resource.error(dataa, null)
                }
            })


        } else {
//            cover_image = RequestBody.create(".png".toMediaTypeOrNull(), _cover_image)
//            var tatee = ""
//            var _selectedUsers: RequestBody? = null

            builder.addFormDataPart(
                "cover_image",
                _cover_image.getName(),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), _cover_image)
            )

            if (selectedusers != null) {
                if (selectedusers.isNotEmpty()) {
                    for (i in 0 until selectedusers.size) {
/*
                        tatee = if (i == 0) {
                          "user_ids[$i]:" + selectedusers.get(i).userID.toString()
                        } else {
                            tatee + ", user_ids[$i]:" + selectedusers.get(i).userID.toString()
                        }
*/
                        builder.addFormDataPart(
                            "user_ids[" + i + "]",
                            selectedusers.get(i).userID.toString()
                        )
                    }
//                    _selectedUsers = RequestBody.create("text/plain".toMediaTypeOrNull(), tatee)
                }
            }

            val requestBody = builder.build()

            apiInterface.journalUpdate(
                mytoken,/*
                cover_image,
                category_id,
                title,
                cover_bc,
                description,
                html_content,
                protection,
                is_active,*/
                requestBody,
//                j_ID
            ).enqueue(object : Callback<APIInterface.ApiResponse<Journals>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<Journals>?>?,
                    response: Response<APIInterface.ApiResponse<Journals>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<Journals> = response.body()!!

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
                    call: Call<APIInterface.ApiResponse<Journals>>,
                    t: Throwable?
                ) {
                    var dataa = t?.message.toString()
                    var mydata = t?.localizedMessage

                    data.value = Resource.error(dataa, null)
                }
            })
        }



        return data
    }


    fun imageUpload(
        _token: String,
        _cover_image: File
    ): MutableLiveData<Resource<APIInterface.ApiResponse<ImageUploadModel>>> {

        var cover_image: RequestBody
        cover_image = RequestBody.create(".png".toMediaTypeOrNull(), _cover_image)


        val data: MutableLiveData<Resource<APIInterface.ApiResponse<ImageUploadModel>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<ImageUploadModel>>>()

        val mytoken = "Bearer $_token"
        apiInterface.imageUpload(
            mytoken,
            cover_image,
        ).enqueue(object : Callback<APIInterface.ApiResponse<ImageUploadModel>> {
            override fun onResponse(
                call: Call<APIInterface.ApiResponse<ImageUploadModel>?>?,
                response: Response<APIInterface.ApiResponse<ImageUploadModel>>
            ) {
                if (response.body() != null) {
                    val body: APIInterface.ApiResponse<ImageUploadModel> = response.body()!!

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
                call: Call<APIInterface.ApiResponse<ImageUploadModel>>,
                t: Throwable?
            ) {
                var dataa = t?.message.toString()
                var mydata = t?.localizedMessage

                data.value = Resource.error(dataa, null)
            }
        })
        return data
    }


    fun getTemplate(
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<List<JournalTemplateModel>>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<List<JournalTemplateModel>>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<List<JournalTemplateModel>>>>()

        val mytoken = "Bearer $token"
        apiInterface.getTemplate(mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<List<JournalTemplateModel>>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<List<JournalTemplateModel>>>,
                    response: Response<APIInterface.ApiResponse<List<JournalTemplateModel>>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<List<JournalTemplateModel>> =
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
                    call: Call<APIInterface.ApiResponse<List<JournalTemplateModel>>>,
                    t: Throwable?
                ) {
                    var dataa = t?.message.toString()
                    var mydata = t?.localizedMessage

                    data.value = Resource.error(dataa, null)
                }
            })
        return data
    }


    fun getCategoriesList(
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<List<Categories>>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<List<Categories>>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<List<Categories>>>>()

        val mytoken = "Bearer $token"
        apiInterface.getCategoriesList(mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<List<Categories>>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<List<Categories>>>,
                    response: Response<APIInterface.ApiResponse<List<Categories>>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<List<Categories>> =
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
                    call: Call<APIInterface.ApiResponse<List<Categories>>>,
                    t: Throwable?
                ) {
                    var dataa = t?.message.toString()
                    var mydata = t?.localizedMessage

                    data.value = Resource.error(dataa, null)
                }
            })
        return data
    }


    // ------------------------------------------------------------------------------------------------
    // Comments


    fun getAllComments(
        commentsId: Int,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<List<CommentsDataParent>>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<List<CommentsDataParent>>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<List<CommentsDataParent>>>>()

        val mytoken = "Bearer $token"
        apiInterface.getComments(commentsId, mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<List<CommentsDataParent>>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<List<CommentsDataParent>>>,
                    response: Response<APIInterface.ApiResponse<List<CommentsDataParent>>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<List<CommentsDataParent>> =
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
                    call: Call<APIInterface.ApiResponse<List<CommentsDataParent>>>,
                    t: Throwable?
                ) {
                    var dataa = t?.message.toString()
                    var mydata = t?.localizedMessage

                    data.value = Resource.error(dataa, null)
                }
            })
        return data
    }


    fun commentLike(
        comment_ID: Int,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>()

        val mytoken = "Bearer $token"
        apiInterface.commentLike(comment_ID, mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<JsonElement>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<JsonElement>?>?,
                    response: Response<APIInterface.ApiResponse<JsonElement>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<JsonElement> = response.body()!!

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

    fun commenteDelete(
        comment_ID: Int,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>()

        val mytoken = "Bearer $token"
        apiInterface.deleteComment(comment_ID, mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<JsonElement>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<JsonElement>?>?,
                    response: Response<APIInterface.ApiResponse<JsonElement>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<JsonElement> = response.body()!!

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


    fun sendComment(
        journal_ID: Int,
        comment: String,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<SendCommentData>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<SendCommentData>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<SendCommentData>>>()

        val mytoken = "Bearer $token"
        apiInterface.sendComment(journal_ID, comment, mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<SendCommentData>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<SendCommentData>?>?,
                    response: Response<APIInterface.ApiResponse<SendCommentData>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<SendCommentData> = response.body()!!

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
                    call: Call<APIInterface.ApiResponse<SendCommentData>>,
                    t: Throwable?
                ) {
                    var dataa = t?.message.toString()
                    var mydata = t?.localizedMessage

                    data.value = Resource.error(dataa, null)
                }
            })
        return data
    }

    fun sendComment(
        journal_ID: Int,
        comment: String,
        comment_Id: String,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<SendCommentData>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<SendCommentData>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<SendCommentData>>>()

        val mytoken = "Bearer $token"
        apiInterface.sendComment(journal_ID, comment, comment_Id, mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<SendCommentData>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<SendCommentData>?>?,
                    response: Response<APIInterface.ApiResponse<SendCommentData>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<SendCommentData> = response.body()!!

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
                    call: Call<APIInterface.ApiResponse<SendCommentData>>,
                    t: Throwable?
                ) {
                    var dataa = t?.message.toString()
                    var mydata = t?.localizedMessage

                    data.value = Resource.error(dataa, null)
                }
            })
        return data
    }

    fun reportComment(
        commentTitle: String,
        commentDescription: String,
        comment_ID: Int,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>()
        var mytoken = "Bearer $token"

        apiInterface.commentReport(comment_ID!!, commentTitle, commentDescription, mytoken).enqueue(
            object : Callback<APIInterface.ApiResponse<JsonElement>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<JsonElement>>,
                    response: Response<APIInterface.ApiResponse<JsonElement>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<JsonElement> = response.body()!!

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
                    call: Call<APIInterface.ApiResponse<JsonElement>>,
                    t: Throwable
                ) {
                    var dataa = t?.message.toString()
                    var mydata = t?.localizedMessage

                    data.value = Resource.error(dataa, null)
                }
            }
        )
        return data
    }

    fun commentUpdate(
        journalId: Int,
        commentsId: Int,
        comment: String,
        token: String
    ): MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>> {
        val data: MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>> =
            MutableLiveData<Resource<APIInterface.ApiResponse<JsonElement>>>()

        val mytoken = "Bearer $token"
        apiInterface.commentUpdate(journalId, commentsId, comment, mytoken)
            .enqueue(object : Callback<APIInterface.ApiResponse<JsonElement>> {
                override fun onResponse(
                    call: Call<APIInterface.ApiResponse<JsonElement>>,
                    response: Response<APIInterface.ApiResponse<JsonElement>>
                ) {
                    if (response.body() != null) {
                        val body: APIInterface.ApiResponse<JsonElement> =
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


}