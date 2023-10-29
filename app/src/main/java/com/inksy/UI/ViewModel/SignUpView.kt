package com.inksy.UI.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.inksy.Model.UserModel
import com.inksy.Remote.APIInterface
import com.inksy.UI.Repositories.SignUpRepo


class SignUpView : ViewModel() {

    private var mutableLiveData: MutableLiveData<APIInterface.ApiResponse<UserModel>>? = null
    private var signUpRepo: SignUpRepo? = null

    fun init() {
        signUpRepo = SignUpRepo.getInstance()
        if (mutableLiveData != null) {
            return
        }


    }


}