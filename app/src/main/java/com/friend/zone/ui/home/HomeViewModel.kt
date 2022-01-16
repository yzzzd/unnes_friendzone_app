package com.friend.zone.ui.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.friend.zone.App
import com.friend.zone.api.ApiService
import com.friend.zone.data.Const
import com.friend.zone.data.User
import com.google.gson.Gson
import com.nuryazid.core.api.ApiObserver
import com.nuryazid.core.data.CoreSession
import com.nuryazid.core.extension.toList
import com.nuryazid.core.extension.toObject

class HomeViewModel: ViewModel() {

    private val apiService = ApiService.getInstance()

    private val context: Context by lazy {
        App.context!!
    }

    val user = CoreSession(context).getString(Const.SESSION.USER).toObject<User>(Gson())

    val friends = MutableLiveData<List<User>>()

    val showLoading = MutableLiveData<Boolean>()

    fun getFriendList() {
        showLoading.postValue(true)
        ApiObserver({ apiService.listFriend() }, true) {
            val data = it.getJSONArray("data")
            val friendData = data.toList<User>(Gson())
            friends.postValue(friendData)
            showLoading.postValue(false)
        }
    }
}