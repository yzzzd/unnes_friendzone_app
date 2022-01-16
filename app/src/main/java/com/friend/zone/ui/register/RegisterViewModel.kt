package com.friend.zone.ui.register

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.friend.zone.App
import com.friend.zone.api.ApiService
import com.nuryazid.core.api.ApiObserver
import com.nuryazid.core.api.ApiResponse

class RegisterViewModel: ViewModel() {

    private val apiService = ApiService.getInstance()

    private val context: Context by lazy {
        App.context!!
    }

    val apiResponse = MutableLiveData<ApiResponse>()

    fun register(name: String, phone: String, password: String) {

        if (name.isNotEmpty() && phone.isNotEmpty() && password.isNotEmpty()) {

            apiResponse.postValue(ApiResponse().responseLoading("Mendaftarkan..."))

            ApiObserver({ apiService.register(name, phone, password) }) {
                apiResponse.postValue(ApiResponse().responseSuccess("Berhasil mendaftar"))
            }.onError {
                apiResponse.postValue(it)
            }
        } else {
            apiResponse.postValue(ApiResponse().responseError().apply { message = "Harap lengkapi semua form data pendaftaran" })
        }
    }
}