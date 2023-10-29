package com.inksy.UI.Repositories

import androidx.lifecycle.MutableLiveData
import com.inksy.Model.UserModel
import com.inksy.Remote.APIClient
import com.inksy.Remote.APIInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignUpRepo {
    var apiInterface: APIInterface = APIClient.createService(APIInterface::class.java)

    companion object {
        lateinit var signupRepo: SignUpRepo

        fun getInstance(): SignUpRepo {

            signupRepo = SignUpRepo()

            return signupRepo
        }
    }




}
