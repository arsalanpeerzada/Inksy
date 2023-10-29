package com.inksy.Remote

import com.example.example.DoodleData
import com.example.example.DoodlePack
import com.google.gson.JsonElement
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.inksy.Model.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


public interface APIInterface {


//    ----------------------------------------------------------------------------------------
    // Login

    @FormUrlEncoded
    @POST("login")
    @Headers("Accept: application/json")
    fun login(
        @Field("email") email: String?,
        @Field("password") password: String?,
        @Field("phone") mobile: String?,
        @Field("phone_code") code: String?,
        @Field("device_type") device_type: String?,
        @Field("device_token") device_token: String?
    ): Call<ApiResponse<UserModel>>

    @FormUrlEncoded
    @POST("login")
    @Headers("Accept: application/json")
    fun loginRegister(
        @Field("email") email: String?,
        @Field("password") password: String?,
        @Field("phone") mobile: String?,
        @Field("phone_code") code: String?,
        @Field("token") tokencode: String?,
        @Field("device_type") device_type: String?,
        @Field("device_token") device_token: String?
    ): Call<ApiResponse<UserModel>>

    @Headers("Accept: application/json")
    @POST("logout")
    fun logout(
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>

    @Multipart
    @POST("user/profile")
    @Headers("Accept: application/json")
    fun profile(
        @Part("avatar\"; filename=\"myfile.jpg") filePart: RequestBody,
        @Part("full_name") full_name: RequestBody?,
        @Part("bio") bio: RequestBody?,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<UserModel>>


    @POST("user/profile/privacy/change")
    @Headers("Accept: application/json")
    fun privacyChange(
        @Header("Authorization") token: String?
    ): Call<ApiResponse<UserModel>>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("user/password/change")
    fun changePassword(
        @Field("old_password") old_password: String?,
        @Field("password") password: String?,
        @Field("password_confirmation") password_confirmation: String?,
        @Field("email") email: String?,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("reset-password")
    fun resetPassword(
        @Field("password") password: String?,
        @Field("token") code: String?,
        @Field("email") email: String?,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("forgot-password")
    fun forgotPassword(
        @Field("email") email: String?,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("verify-code")
    fun verifyCode(
        @Field("token") code: String?,
        @Field("email") email: String?,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>


    @FormUrlEncoded
    @POST("user/contact/message")
    @Headers("Accept: application/json")
    fun contactUs(
        @Field("subject") subject: String,
        @Field("message") message: String,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>


    @FormUrlEncoded
    @POST("user/category/suggestion")
    @Headers("Accept: application/json")
    fun suggestion(
        @Field("category_name") category_name: String,
        @Field("description") description: String,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("phone-verify")
    fun phoneVerify(
        @Field("phone_code") code: String?,
        @Field("phone") email: String?,
    ): Call<ApiResponse<NumberVerifyModel>>


    // ----------------------------------------------------------------------------------------------
    // User

    @Multipart
    @POST("user/profile")
    @Headers("Accept: application/json")
    fun profile(
        @Part("full_name") full_name: RequestBody?,
        @Part("bio") bio: RequestBody?,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<UserModel>>


    @GET("user/blocked/list")
    @Headers("Accept: application/json")
    fun blockList(
        @Header("Authorization") token: String?
    ): Call<ApiResponse<List<UserModel>>>


    @GET("user/follow-following/list")
    @Headers("Accept: application/json")
    fun followersFollowList(
        @Header("Authorization") token: String?
    ): Call<ApiResponse<List<UserModel>>>


//    @GET("user/follow-following/list")
//    @Headers("Accept: application/json")
//    fun followersFollowList(
//        @Header("Authorization") token: String?
//    ): Call<ApiResponse<List<UserModel>>>

    @GET("user/profile/{id}")
    @Headers("Accept: application/json")
    fun userDetail(
        @Path("id") id: Int,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<UserModel>>

    @GET("user/follow/requests")
    @Headers("Accept: application/json")
    fun followRequests(
        @Header("Authorization") token: String?
    ): Call<ApiResponse<List<UserModel>>>

    @POST("user/block/{id}")
    @Headers("Accept: application/json")
    fun userBlock(
        @Path("id") id: Int,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>

    @POST("user/unblock/{id}")
    @Headers("Accept: application/json")
    fun userUnblock(
        @Path("id") id: Int,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>

    @FormUrlEncoded
    @POST("journal/appeal")
    @Headers("Accept: application/json")
    fun askForActivation(
        @Field("journal_id") journal_id: Int,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>

    @FormUrlEncoded
    @POST("user/report")
    @Headers("Accept: application/json")
    fun userReport(
        @Field("user_id") user_id: Int,
        @Field("title") title: String,
        @Field("description") description: String,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>

    @POST("user/follow/{id}")
    @Headers("Accept: application/json")
    fun userFollow(
        @Path("id") id: Int,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>

    @FormUrlEncoded
    @POST("user/follow/request")
    @Headers("Accept: application/json")
    fun userRequest(
        @Field("user_id") user_id: Int,
        @Field("is_accept") is_accept: Int,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>

    @POST("user/unfollow/{id}")
    @Headers("Accept: application/json")
    fun userUnfollow(
        @Path("id") id: Int,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>

    @Headers("Accept: application/json")
    @GET("user/search")
    fun searchUser(
        @Query("title") s: String?,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<List<UserModel>>>

    @GET("user/notifications")
    @Headers("Accept: application/json")
    fun notificationsList(
        @Header("Authorization") token: String?
    ): Call<ApiResponse<List<NotificationDataModel>>>

    @GET("chat/nodes")
    @Headers("Accept: application/json")
    fun chatList(
        @Header("Authorization") token: String?
    ): Call<ApiResponse<List<ChatDataModel>>>

    @FormUrlEncoded
    @POST("chat/notification")
    @Headers("Accept: application/json")
    fun sendMessageNotification(
        @Field("receiver_id") receiver_id: Int,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>

    @FormUrlEncoded
    @POST("chat/node")
    @Headers("Accept: application/json")
    fun createChatNode(
        @Field("receiver_id") receiver_id: Int,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>

    @FormUrlEncoded
    @POST("user/notifications/clear")
    @Headers("Accept: application/json")
    fun clearNotifications(
        @Field("empty_field") empty_field: String?,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>

    // ------------------------------------------------------------------------------
    //Doodle

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("doodle/remove")
    fun deleteDoodle(
        @Field("doodle_id") doodle_id: Int,
        @Field("doodle_pack_id") doodle_pack_id: Int,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("doodle/pack/delete")
    fun deletePack(
        @Field("doodle_pack_id") doodle_pack_id: Int,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>

    @Multipart
    @Headers("Accept: application/json")
    @POST("doodle/pack/update")
    fun updateDoodlePack(
        @Part("pack_title") pack_title: RequestBody,
        @Part("price") price: RequestBody,
        @Part("id") doodle_pack_id: RequestBody,
        @Part("cover_image\"; filename=\"myfile.jpg") cover_image: RequestBody,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>

//    @FormUrlEncoded
//    @Headers("Accept: application/json")
//    @POST("doodle/pack/update")
//    fun updateDoodlePacktest(
//        @Part("pack_title") pack_title: String,
//        @Part("price") price: String,
//        @Part("id") doodle_pack_id: String,
//        @Part("cover_image") cover_image: String,
//        @Header("Authorization") token: String?
//    ): Call<ApiResponse<JsonElement>>


    @GET("doodle/shop")
    @Headers("Accept: application/json")
    fun doodleShop(
        @Header("Authorization") token: String?
    ): Call<ApiResponse<DoodleData>>

    @GET("doodle/shop/all")
    @Headers("Accept: application/json")
    fun doodleShopAll(
        @Header("Authorization") token: String?
    ): Call<ApiResponse<DoodleData>>

    @GET("doodle/pack/pendings")
    @Headers("Accept: application/json")
    fun doodlePending(
        @Header("Authorization") token: String?
    ): Call<ApiResponse<List<DoodlePack>>>

    @GET("doodle/pack/approved")
    @Headers("Accept: application/json")
    fun doodleApproved(
        @Header("Authorization") token: String?
    ): Call<ApiResponse<List<DoodlePack>>>

    @GET("doodle/pack/purchased")
    @Headers("Accept: application/json")
    fun doodlePurchased(
        @Header("Authorization") token: String?
    ): Call<ApiResponse<List<DoodlePack>>>


    @GET("artist/dashboard")
    @Headers("Accept: application/json")
    fun artistDashboard(
        @Header("Authorization") token: String?,
        @Query("type") type: String?,
    ): Call<ApiResponse<AnalyticsData>>

    @Multipart
    @POST("doodle/pack/create")
    @Headers("Accept: application/json")
    fun createPack(
        @Header("Authorization") token: String?,
        @Part("pack_title") pack_title: RequestBody,
        @Part("price") price: RequestBody,
        @Part("cover_image\"; filename=\"myfile.jpg") default_cover: RequestBody,
    ): Call<ApiResponse<CreateDoodleModel>>

    @POST("artist/make")
    @Headers("Accept: application/json")
    fun artistMake(
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>


    @Headers("Accept: application/json")
    @GET("doodle/search")
    fun searchDoodle(
        @Query("title") s: String?,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<List<DoodlePack>>>

    @FormUrlEncoded
    @POST("order/create")
    @Headers("Accept: application/json")
    fun orderCreate(
        @Field("doodle_pack_id") doodle_pack_id: Int,
        @Field("amount") amount: String,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>

    @FormUrlEncoded
    @POST("user/payment-method")
    @Headers("Accept: application/json")
    fun paymentMethod(
        @Field("payment_email") payment_email: String,
        @Field("payment_method") payment_method: String,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>

    @GET("doodle/pack/details/{id}")
    @Headers("Accept: application/json")
    fun doodleDetails(
        @Path("id") id: String,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<DoodlePack>>


    @GET("artist/transactions")
    @Headers("Accept: application/json")
    fun artistTransaction(
        @Header("Authorization") token: String?
    ): Call<ApiResponse<List<TransactionModel>>>

    @Multipart
    @POST("doodle/add")
    @Headers("Accept: application/json")
    fun addDoodle(
        @Part("doodle_pack_id") id: RequestBody,
        @Part("doodle_image\"; filename=\"myfile.jpg") doodle_image: RequestBody,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<DoodlePack>>

    //------------------------------------------------------------------------------------------
    // Journal


    @GET("categories")
    @Headers("Accept: application/json")
    fun getCategoriesList(
        @Header("Authorization") token: String?
    ): Call<ApiResponse<List<Categories>>>

    @GET("journal/templates")
    @Headers("Accept: application/json")
    fun getTemplate(
        @Header("Authorization") token: String?
    ): Call<ApiResponse<List<JournalTemplateModel>>>


    @GET("journal/comments/{id}")
    @Headers("Accept: application/json")
    fun getComments(
        @Path("id") commentId: Int,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<List<CommentsDataParent>>>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("journal/comment/delete")
    fun deleteComment(
        @Field("comment_id") comment_id: Int,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>

    @FormUrlEncoded
    @POST("journal/comment/update")
    @Headers("Accept: application/json")
    fun commentUpdate(
        @Field("journal_id") journal_id: Int,
        @Field("comment_id") comment_id: Int,
        @Field("comment") comment: String,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("journal/comment/like")
    fun commentLike(
        @Field("comment_id") commentId: Int?,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("journal/comment")
    fun sendComment(
        @Field("journal_id") journalId: Int?,
        @Field("comment") comment: String?,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<SendCommentData>>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("journal/comment")
    fun sendComment(
        @Field("journal_id") journalId: Int?,
        @Field("comment") comment: String?,
        @Field("comment_id") comment_id: String,
        @Header("Authorization") token: String?,
    ): Call<ApiResponse<SendCommentData>>

    @FormUrlEncoded
    @POST("journal/comment/report")
    @Headers("Accept: application/json")
    fun commentReport(
        @Field("comment_id") journal_id: Int,
        @Field("title") title: String,
        @Field("description") description: String,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>

    @FormUrlEncoded
    @POST("journal/report")
    @Headers("Accept: application/json")
    fun journalReport(
        @Field("journal_id") comment: Int,
        @Field("title") title: String,
        @Field("description") description: String,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>


    @Headers("Accept: application/json")
    @GET("journal/search")
    fun searchJournal(
        @Query("title") s: String?,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<List<Journals>>>

    @Headers("Accept: application/json")
    @GET("journal/search")
    fun searchCategory(
        @Query("category_title") s: String?,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<List<Journals>>>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("journal/like")
    fun journalLike(
        @Field("journal_id") journal_Id: String?,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("journal/delete")
    fun journalDelete(
        @Field("journal_id") journal_Id: String?,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>

    @GET("journal/details/{id}")
    @Headers("Accept: application/json")
    fun journalDetail(
        @Path("id") id: Int,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<Journals>>


/*
    @Multipart
    @Headers("Accept: application/json")
    @POST("journal/create")
    fun journalCreate(
        @Header("Authorization") token: String?,
        @Part("cover_image\"; filename=\"myfile.jpg") coverImage: RequestBody,
        @Part("category_id") category_id: RequestBody,
        @Part("title") title: RequestBody?,
        @Part("cover_bc") cover_bc: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("html_content") html_content: RequestBody?,
        @Part("protection") protection: RequestBody?,
        @Part("is_active") is_active: RequestBody?,
        @Part("user_ids[]") users: RequestBody?
    ): Call<ApiResponse<Journals>>
*/

//    @Multipart
    @Headers("Accept: application/json")
    @POST("journal/create")
    fun journalCreate(
        @Header("Authorization") token: String?,
        @Body data: RequestBody?
    ): Call<ApiResponse<Journals>>


    @Headers("Accept: application/json")
    @POST("journal/create")
    fun journalCreate1(
        @Header("Authorization") token: String?,
        @Body data: RequestBody?
    ): Call<ApiResponse<Journals>>
/*
    @Multipart
    @Headers("Accept: application/json")
    @POST("journal/update")
    fun journalUpdate(
        @Header("Authorization") token: String?,
        @Part("cover_image\"; filename=\"myfile.jpg") coverImage: RequestBody,
        @Part("category_id") category_id: RequestBody,
        @Part("title") title: RequestBody?,
        @Part("cover_bc") cover_bc: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("html_content") html_content: RequestBody?,
        @Part("protection") protection: RequestBody?,
        @Part("is_active") is_active: RequestBody?,
        @Part("user_ids[]") users: RequestBody?,
        @Part("journal_id") journal_Id: RequestBody?
    ): Call<ApiResponse<Journals>>
*/

//    @Multipart
    @Headers("Accept: application/json")
    @POST("journal/update")
    fun journalUpdate(
        @Header("Authorization") token: String?,
        @Body data: RequestBody?
    ): Call<ApiResponse<Journals>>

/*
    @Multipart
    @Headers("Accept: application/json")
    @POST("journal/update")
    fun journalUpdate2(
        @Header("Authorization") token: String?,
        @Part("category_id") category_id: RequestBody,
        @Part("title") title: RequestBody?,
        @Part("cover_bc") cover_bc: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("html_content") html_content: RequestBody?,
        @Part("protection") protection: RequestBody?,
        @Part("is_active") is_active: RequestBody?,
        @Part("user_ids[]") users: RequestBody?,
        @Part("journal_id") journal_Id: RequestBody?
    ): Call<ApiResponse<Journals>>
*/

//    @Multipart
    @Headers("Accept: application/json")
    @POST("journal/update")
    fun journalUpdate2(
        @Header("Authorization") token: String?,
        @Body data: RequestBody?
    ): Call<ApiResponse<Journals>>


    @Multipart
    @Headers("Accept: application/json")
    @POST("image/upload")
    fun imageUpload(
        @Header("Authorization") token: String?,
        @Part("avatar\"; filename=\"myfile.jpg") avatar: RequestBody,
    ): Call<ApiResponse<ImageUploadModel>>


    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("journal/follow")
    fun journalFollow(
        @Field("journal_id") journal_Id: String?,
        @Header("Authorization") token: String?
    ): Call<ApiResponse<JsonElement>>


    @GET("page/about")
    fun about(
        @Header("Authorization") token: String?
    ): Call<ApiResponse<OthersModel>>

    @GET("page/community-guidelines")
    fun community(
        @Header("Authorization") token: String?
    ): Call<ApiResponse<OthersModel>>

    @GET("page/terms-and-condition")
    fun terms(
        @Header("Authorization") token: String?
    ): Call<ApiResponse<OthersModel>>

    @GET("page/privacy-policy")
    fun privacy_policy(
        @Header("Authorization") token: String?
    ): Call<ApiResponse<OthersModel>>


    @GET("dashboard")
    @Headers("Accept: application/json")
    fun dashboardData(
        @Header("Authorization") token: String?
    ): Call<ApiResponse<DashboardDataModel>>


    class ApiResponse<T> {
        @SerializedName("success")
        @Expose
        var status: Int? = null

        @SerializedName("message")
        @Expose
        var message: String? = null

        @SerializedName("data")
        @Expose
        var data: T? = null
            private set

        fun setData(data: T) {
            this.data = data
        }
    }
}