package com.friend.zone.ui.detail

import android.os.Bundle
import com.friend.zone.R
import com.friend.zone.data.Const
import com.friend.zone.databinding.ActivityFriendDetailBinding
import com.nuryazid.core.base.activity.CoreActivity

class FriendDetailActivity : CoreActivity<ActivityFriendDetailBinding, FriendDetailViewModel>(R.layout.activity_friend_detail) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.friend = intent.getParcelableExtra(Const.INTENT.DATA)
    }
}