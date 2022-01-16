package com.friend.zone.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import com.friend.zone.R
import com.friend.zone.data.User
import com.friend.zone.databinding.ActivityHomeBinding
import com.friend.zone.databinding.ItemFriendBinding
import com.friend.zone.ui.login.LoginActivity
import com.friend.zone.ui.profile.UpdateProfileActivity
import com.nuryazid.core.base.activity.CoreActivity
import com.nuryazid.core.base.adapter.CoreListAdapter
import com.nuryazid.core.data.CoreSession

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
        val intentProfile = Intent(this,  UpdateProfileActivity::class.java )
        startActivity(intentProfile)
    }

    fun logout() {
        AlertDialog.Builder(this)
            .setMessage("Apakah kamu yakin akan keluar?")
            .setPositiveButton("Keluar") { dialog, i ->
                dialog.dismiss()
                CoreSession(this).clearAll()
                val loginIntent = Intent(this, LoginActivity::class.java)
                startActivity(loginIntent)
                finish()
            }
            .setNegativeButton("Batal") { dialog, i ->
                dialog.dismiss()
            }
            .show()
    }
}