package com.friend.zone.ui.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.friend.zone.App
import com.friend.zone.api.ApiService
import com.friend.zone.data.Const
import com.nuryazid.core.api.ApiObserver
import com.nuryazid.core.api.ApiResponse
import com.nuryazid.core.data.CoreSession

class LoginViewModel: ViewModel() {

    private val apiService = ApiService.getInstance()

    private val context: Context by lazy {
        App.context!!
    }

    val apiResponse = MutableLiveData<ApiResponse>()

    fun login(phone: String, password: String) {

        if (phone.isNotEmpty() && password.isNotEmpty()) {

            apiResponse.postValue(ApiResponse().responseLoading("Masuk..."))

            initToken {
                ApiObserver({ apiService.login(phone, password, "null") }) {
                    val data = it.getJSONObject("data")
                    CoreSession(context).setValue(Const.SESSION.USER, data.toString())
                    apiResponse.postValue(ApiResponse().responseSuccess())
                }.onError {
                    apiResponse.postValue(it)
                }
            }
        } else {
            apiResponse.postValue(ApiResponse().responseError().apply { message = "Harap masukkan telepon dan kata sandi terlebih dahulu" })
        }
    }

    private fun initToken(result: () -> Unit) {
        CoreSession(context).setValue(Const.SESSION.TOKEN, "")
        ApiObserver({ apiService.requestToken() }) {
            val data = it.getJSONObject("data")
            val token = data.getString("token")
            CoreSession(context).setValue(Const.SESSION.TOKEN, token)
            result()
        }.onError {
            apiResponse.postValue(it)
        }
    }
}