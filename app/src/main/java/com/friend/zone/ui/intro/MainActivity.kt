package com.friend.zone.ui.intro

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.friend.zone.R
import com.friend.zone.databinding.ActivityMainBinding
import com.friend.zone.ui.login.LoginActivity
import com.nuryazid.core.base.activity.BasicActivity

class MainActivity : BasicActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                val loginIntent = Intent(this, LoginActivity::class.java)
                startActivity(loginIntent)
                finish()
            }, 2000
        )

    }
}