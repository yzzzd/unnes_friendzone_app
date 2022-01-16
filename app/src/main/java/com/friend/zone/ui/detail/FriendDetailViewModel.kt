package com.friend.zone.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.friend.zone.api.ApiService
import com.friend.zone.data.User
import com.nuryazid.core.api.ApiObserver

class FriendDetailViewModel : ViewModel() {

    private val apiService = ApiService.getInstance()

    val showButtonLike = MutableLiveData(true)

    val friend = MutableLiveData<User>()

    fun likeFriend(id: Int?) {
        showButtonLike.postValue(false)
        ApiObserver({ apiService.likeUser(id) }, true) {
            showButtonLike.postValue(true)

            val current = friend.value?.apply {
                liked = !liked!!
            }

            friend.postValue(current)
        }
    }
}