package com.friend.zone.ui.profile

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.friend.zone.App
import com.friend.zone.api.ApiService
import com.friend.zone.data.Const
import com.friend.zone.data.User
import com.google.gson.Gson
import com.nuryazid.core.api.ApiObserver
import com.nuryazid.core.api.ApiResponse
import com.nuryazid.core.data.CoreSession
import com.nuryazid.core.extension.toObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UpdateProfileViewModel: ViewModel() {
    private val apiService = ApiService.getInstance()

    private val context: Context by lazy {
        App.context!!
    }

    val user = CoreSession(context).getString(Const.SESSION.USER).toObject<User>(Gson())

    val apiResponse = MutableLiveData<ApiResponse>()

    fun update(name: String, about: String, password: String, photo: File?) {

        if (name.isNotEmpty()) {

            apiResponse.postValue(ApiResponse().responseLoading("Mengirim..."))

            val newPassword = if (password.isEmpty()) null else password

            val newPhoto = if (photo != null) {
                val fileBody = photo.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("photo", photo.name, fileBody)
            } else {
                null
            }

            ApiObserver({ apiService.updateProfile(name, about, newPassword, newPhoto) }) {
                val data = it.getJSONObject("data")
                CoreSession(context).setValue(Const.SESSION.USER, data.toString())
                apiResponse.postValue(ApiResponse().responseSuccess("Berhasil update profile"))
            }.onError {
                apiResponse.postValue(it)
            }
        } else {
            apiResponse.postValue(ApiResponse().responseError().apply { message = "Nama tidak boleh kosong" })
        }
    }
}