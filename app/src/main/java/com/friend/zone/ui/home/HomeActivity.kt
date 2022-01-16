package com.friend.zone.ui.home

import android.content.Intent
import android.os.Bundle
import com.friend.zone.R
import com.friend.zone.data.Const
import com.friend.zone.data.User
import com.friend.zone.databinding.ActivityHomeBinding
import com.friend.zone.databinding.ItemFriendBinding
import com.nuryazid.core.base.activity.CoreActivity
import com.nuryazid.core.base.adapter.CoreListAdapter

class HomeActivity : CoreActivity<ActivityHomeBinding, HomeViewModel>(R.layout.activity_home) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.activity = this

        viewModel.friends.observe(this, {
            binding.adapter = CoreListAdapter<ItemFriendBinding, User>(R.layout.item_friend)
                .initItem(it.toMutableList() as ArrayList<User?>) { position, data ->
                    /*val intentDetail = Intent(this, FriendDetailActivity::class.java).apply {
                        putExtra(Const.INTENT.DATA, data)
                    }
                    startActivity(intentDetail)*/
                }
        })

        viewModel.getFriendList()
    }

    fun openProfile() {
        /*val intentProfile = Intent(this,  UpdateProfileActivity::class.java )
        startActivity(intentProfile)*/
    }
}